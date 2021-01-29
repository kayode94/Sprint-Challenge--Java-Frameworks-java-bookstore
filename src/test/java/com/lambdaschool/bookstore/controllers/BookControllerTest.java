package com.lambdaschool.bookstore.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.bookstore.BookstoreApplication;
import com.lambdaschool.bookstore.models.Book;
import com.lambdaschool.bookstore.models.Wrote;
import com.lambdaschool.bookstore.services.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookstoreApplication.class)
@WithMockUser(username = "admin", roles = {"ADMIN", "DATA"})
public class BookControllerTest
{
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    List<Book> myBookList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void listAllBooks() throws Exception
    {
        String apiUrl = "/books/books";
        Mockito.when(bookService.findAll()).thenReturn(myBookList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rb).andReturn();
        String testResult = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = mapper.writeValueAsString(myBookList);
        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getBookById()throws Exception
    {
        String apiUrl = "/books/book.1";
        Mockito.when(bookService.findBookById(1)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rb).andReturn();
        String testResult = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = "";

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void getNoBookById() throws Exception
    {
        String apiUrl = "/books/book/101";
        Mockito.when(bookService.findBookById(101)).thenReturn(null);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(rb).andReturn();
        String testResult = result.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String expectedResult = "";

        assertEquals(expectedResult, testResult);
    }

    @Test
    public void addNewBook() throws Exception
    {
        String book3name = "another book";
        Book book3 = new Book("The Da Vinci Code", "9780307474278", 2009, s1);
        book3.getWrotes().add(new Wrote(a2, new Book()));
        book3 = bookService.save(book3);
        String apiUrl = "/books/book";

        ObjectMapper mapper = new ObjectMapper();
        String bookString = mapper.writeValueAsString(book3);

        Mockito.when(bookService.save(any(Book.class))).thenReturn(book3);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(bookString);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateFullBook()
    {

    }

    @Test public void deleteBookById() throws Exception
    {
        String apiUrl = "/books/book/{bookid}";
        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "3").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().is2xxSuccessful()).andDo(MockMvcResultHandlers.print());
    }
}
