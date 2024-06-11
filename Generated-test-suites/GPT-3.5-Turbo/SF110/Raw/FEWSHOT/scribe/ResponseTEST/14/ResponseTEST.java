package org.scribe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.scribe.utils.StreamUtils;

@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {
    
    @Mock
    private HttpURLConnection connection;
    
    @Mock
    private InputStream inputStream;
    
    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        when(connection.getResponseCode()).thenReturn(200);
        when(connection.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void testConstructor_withSuccessfulConnection() throws IOException {
        Response response = new Response(connection);

        verify(connection, times(1)).connect();
        verify(connection, times(1)).getResponseCode();
        verify(connection, times(1)).getInputStream();
        
        assertEquals(200, response.getCode());
        assertEquals(inputStream, response.getStream());
        assertNull(response.getBody());
        assertNull(response.getHeader("headerName"));
    }
    
    @Test
    public void testConstructor_withUnknownHostException() throws IOException {
        when(connection.getResponseCode()).thenThrow(UnknownHostException.class);
        
        Response response = new Response(connection);

        verify(connection, times(1)).connect();
        verify(connection, times(0)).getResponseCode();
        verify(connection, times(0)).getInputStream();
        
        assertEquals(404, response.getCode());
        assertEquals("", response.getBody());
        assertNull(response.getStream());
        assertNull(response.getHeader("headerName"));
    }
    
    @Test
    public void testParseBodyContents() {
        Response response = new Response(connection);
        when(inputStream.toString()).thenReturn("Response Body");

        String body = response.getBody();
        
        assertEquals("Response Body", body);
    }
    
    @Test
    public void testParseHeaders() {
        Response response = new Response(connection);
        when(connection.getHeaderFields()).thenReturn(new HashMap<String, List<String>>());
        when(connection.getHeaderFields().keySet()).thenReturn(new HashSet<String>());
        
        Map<String, String> headers = response.getHeaders();

        assertEquals(0, headers.size());
    }
    
    @Test
    public void testWasSuccessful_withSuccessfulCode() {
        Response response = new Response(connection);
        when(connection.getResponseCode()).thenReturn(200);
        
        boolean wasSuccessful = response.wasSuccessful();

        assertEquals(true, wasSuccessful);
    }
    
    @Test
    public void testWasSuccessful_withNotSuccessfulCode() {
        Response response = new Response(connection);
        when(connection.getResponseCode()).thenReturn(400);
        
        boolean wasSuccessful = response.wasSuccessful();

        assertEquals(false, wasSuccessful);
    }
}