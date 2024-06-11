
package org.scribe.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {
    
    @Test
    public void testGetBody() throws IOException {
        // Arrange
        Response response = new Response(mockHttpURLConnection());
        
        // Act
        String body = response.getBody();
        
        // Assert
        assertNotNull(body);
        assertEquals("MockBody", body);
    }
    
    @Test
    public void testGetStream() throws IOException {
        // Arrange
        Response response = new Response(mockHttpURLConnection());
        
        // Act
        InputStream stream = response.getStream();
        
        // Assert
        assertNotNull(stream);
    }
    
    @Test
    public void testGetCode() throws IOException {
        // Arrange
        Response response = new Response(mockHttpURLConnection());
        
        // Act
        int code = response.getCode();
        
        // Assert
        assertEquals(200, code);
    }
    
    @Test
    public void testGetHeaders() throws IOException {
        // Arrange
        Response response = new Response(mockHttpURLConnection());
        
        // Act
        Map<String, String> headers = response.getHeaders();
        
        // Assert
        assertNotNull(headers);
        assertEquals("MockHeader", headers.get("MockKey"));
    }
    
    @Test
    public void testGetHeader() throws IOException {
        // Arrange
        Response response = new Response(mockHttpURLConnection());
        
        // Act
        String header = response.getHeader("MockKey");
        
        // Assert
        assertNotNull(header);
        assertEquals("MockHeader", header);
    }
    
    private HttpURLConnection mockHttpURLConnection() {
        return new HttpURLConnection(null) {
            @Override
            public void disconnect() { }
            
            @Override
            public boolean usingProxy() { return false; }
            
            @Override
            public void connect() { }
            
            @Override
            public InputStream getErrorStream() { return null; }
            
            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream("MockBody".getBytes());
            }
            
            @Override
            public int getResponseCode() throws IOException { return 200; }
            
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headerFields = new HashMap<>();
                List<String> values = new ArrayList<>();
                values.add("MockHeader");
                headerFields.put("MockKey", values);
                return headerFields;
            }
        };
    }
}
