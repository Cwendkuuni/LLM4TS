package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ResponseTest {
    private HttpURLConnection connection;
    private InputStream inputStream;

    @Before
    public void setup() throws IOException {
        connection = mock(HttpURLConnection.class);
        inputStream = new ByteArrayInputStream("Test Response".getBytes());
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(inputStream);
        when(connection.getErrorStream()).thenReturn(null);
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, String>());
    }

    @Test
    public void testConstructor() throws IOException {
        Response response = new Response(connection);
        
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getHeaders());
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testParseBodyContents() throws IOException {
        Response response = new Response(connection);
        
        assertEquals("Test Response", response.getBody());
    }
    
    @Test
    public void testParseHeaders() throws IOException {
        Response response = new Response(connection);
        
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testWasSuccessful() throws IOException {
        Response response = new Response(connection);
        
        assertEquals(true, response.wasSuccessful());
    }
    
    @Test
    public void testGetBody() throws IOException {
        Response response = new Response(connection);
        
        assertNotNull(response.getBody());
    }
    
    @Test
    public void testGetStream() throws IOException {
        Response response = new Response(connection);
        
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() throws IOException {
        Response response = new Response(connection);
        
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        Response response = new Response(connection);
        
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() throws IOException {
        Response response = new Response(connection);
        
        assertNull(response.getHeader("Test"));
    }
}