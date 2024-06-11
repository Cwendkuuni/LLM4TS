
package org.scribe.model;

import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ResponseTest {
    
    @Test
    public void testGetBody() {
        Response response = new Response(mockConnection());
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(mockConnection());
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(mockConnection());
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(mockConnection());
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(mockConnection());
        assertNull(response.getHeader("Content-Type"));
    }
    
    // Utility method to mock HttpURLConnection
    private HttpURLConnection mockConnection() {
        try {
            HttpURLConnection connection = new HttpURLConnection(new URL("http://example.com")) {
                @Override
                public void disconnect() {
                }

                @Override
                public boolean usingProxy() {
                    return false;
                }

                @Override
                public void connect() throws IOException {
                }

                @Override
                public String getHeaderField(String name) {
                    if (name.equals("Content-Type")) {
                        return "application/json";
                    }
                    return null;
                }

                @Override
                public int getResponseCode() throws IOException {
                    return 200;
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return null;
                }

                @Override
                public InputStream getErrorStream() {
                    return null;
                }
            };
            return connection;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
