import org.junit.Test;
import org.scribe.utils.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;

public class ResponseTest {
    
    @Test
    public void testConstructor_SuccessfulResponse() {
        try {
            HttpURLConnection connection = new MockHttpURLConnection("http://example.com", 200);
            Response response = new Response(connection);
            
            assertEquals(200, response.getCode());
            assertEquals("", response.getBody());
            assertEquals(connection.getInputStream(), response.getStream());
            
            Map<String, String> expectedHeaders = new HashMap<String, String>();
            expectedHeaders.put("Content-Type", "text/plain");
            expectedHeaders.put("Content-Length", "100");
            assertEquals(expectedHeaders, response.getHeaders());
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testConstructor_ConnectionError() {
        try {
            HttpURLConnection connection = new MockHttpURLConnection("http://invalidurl", 404);
            Response response = new Response(connection);
            
            assertEquals(404, response.getCode());
            assertEquals("", response.getBody());
            assertEquals(connection.getErrorStream(), response.getStream());
            
            assertTrue(response.getHeaders().isEmpty());
            
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testGetBody_SuccessfulResponse() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Sample body".getBytes());
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        response.setStream(inputStream);
        
        assertEquals("Sample body", response.getBody());
    }
    
    @Test
    public void testGetBody_ConnectionError() {
        Response response = new Response(new MockHttpURLConnection("http://invalidurl", 404));
        
        assertEquals("", response.getBody());
    }
    
    @Test
    public void testGetStream_SuccessfulResponse() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Sample body".getBytes());
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        response.setStream(inputStream);
        
        assertSame(inputStream, response.getStream());
    }
    
    @Test
    public void testGetCode_SuccessfulResponse() {
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        
        assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders_SuccessfulResponse() {
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        expectedHeaders.put("Content-Type", "text/plain");
        expectedHeaders.put("Content-Length", "100");
        
        assertEquals(expectedHeaders, response.getHeaders());
    }
    
    @Test
    public void testGetHeader_SuccessfulResponse() {
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        
        assertEquals("text/plain", response.getHeader("Content-Type"));
        assertEquals("100", response.getHeader("Content-Length"));
        assertNull(response.getHeader("Invalid-Header"));
    }
    
    // Additional Test Cases
    
    @Test
    public void testGetStream_ConnectionError() {
        Response response = new Response(new MockHttpURLConnection("http://invalidurl", 404));
        
        assertNull(response.getStream());
    }
    
    @Test
    public void testParseBodyContents_EmptyStream() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        response.setStream(inputStream);
        
        assertEquals("", response.parseBodyContents());
    }
    
    @Test
    public void testParseHeaders_EmptyHeaderFields() {
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        
        Map<String, String> expectedHeaders = new HashMap<String, String>();
        
        assertEquals(expectedHeaders, response.parseHeaders(new MockHttpURLConnection("http://example.com", 200)));
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testParseHeaders_NullKeySet() {
        Response response = new Response(new MockHttpURLConnection("http://example.com", 200));
        
        response.parseHeaders(new MockHttpURLConnection("http://example.com", 200)).keySet().iterator().next();
    }
    
    private static class MockHttpURLConnection extends HttpURLConnection {
        
        private int responseCode;
        private InputStream errorStream;
        
        public MockHttpURLConnection(String url, int responseCode) {
            super(null);
            this.responseCode = responseCode;
            this.errorStream = new ByteArrayInputStream("Error body".getBytes());
        }
        
        @Override
        public void disconnect() {}
        
        @Override
        public boolean usingProxy() {
            return false;
        }
        
        @Override
        public void connect() throws IOException {}
        
        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }
        
        @Override
        public InputStream getErrorStream() {
            return errorStream;
        }
        
        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }
        
        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }
        
        @Override
        public Map<String, java.util.List<String>> getHeaderFields() {
            Map<String, java.util.List<String>> headerFields = new HashMap<>();
            ArrayList<String> contentType = new ArrayList<>();
            contentType.add("text/plain");
            ArrayList<String> contentLength = new ArrayList<>();
            contentLength.add("100");
            headerFields.put("Content-Type", contentType);
            headerFields.put("Content-Length", contentLength);
            return headerFields;
        }
    }
}