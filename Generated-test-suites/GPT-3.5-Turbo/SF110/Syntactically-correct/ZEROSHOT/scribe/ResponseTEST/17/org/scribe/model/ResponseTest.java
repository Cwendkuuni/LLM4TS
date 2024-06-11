import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResponseTest {
    
    private Response response;
    private HttpURLConnection connection;
    private Map<String, String> headers;
    private InputStream stream;
    private String body;
    
    @Before
    public void setUp() {
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        body = "{\"name\": \"John\", \"age\": 30}";
        stream = new ByteArrayInputStream(body.getBytes());
        
        connection = new FakeHttpURLConnection(200, headers, stream);
        
        try {
            response = new Response(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetBody() {
        assertEquals(body, response.getBody());
    }
    
    @Test
    public void testGetStream() {
        assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeader() {
        assertEquals("application/json", response.getHeader("Content-Type"));
    }
    
    // Helper class for testing
    private class FakeHttpURLConnection extends HttpURLConnection {
        
        private final int code;
        private final Map<String, String> headers;
        private final InputStream stream;
        
        protected FakeHttpURLConnection(int code, Map<String, String> headers, InputStream stream) {
            super(null);
            this.code = code;
            this.headers = headers;
            this.stream = stream;
        }
        
        @Override
        public int getResponseCode() throws IOException {
            return code;
        }
        
        @Override
        public Map<String, java.util.List<String>> getHeaderFields() {
            Map<String, java.util.List<String>> headerFields = new HashMap<>();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                java.util.List<String> values = new java.util.ArrayList<>();
                values.add(entry.getValue());
                headerFields.put(entry.getKey(), values);
            }
            return headerFields;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return stream;
        }
        
        @Override
        public InputStream getErrorStream() {
            return stream;
        }
        
        @Override
        public void connect() throws IOException {
        
        }
    }
}