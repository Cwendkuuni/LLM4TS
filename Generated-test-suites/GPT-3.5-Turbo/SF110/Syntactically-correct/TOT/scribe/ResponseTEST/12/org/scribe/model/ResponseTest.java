import org.junit.Test;
import org.junit.Assert;

public class ResponseTest {
    
    @Test
    public void testGetBodySuccessful() {
        Response response = new Response(createMockConnection(200));
        Assert.assertEquals("Success", response.getBody());
    }
    
    @Test
    public void testGetBodyError() {
        Response response = new Response(createMockConnection(500));
        Assert.assertEquals("Error", response.getBody());
    }
    
    @Test
    public void testGetBodyNull() {
        Response response = new Response(createMockConnection(404));
        Assert.assertNull(response.getBody());
    }
    
    @Test
    public void testGetStream() {
        Response response = new Response(createMockConnection(200));
        Assert.assertNotNull(response.getStream());
    }
    
    @Test
    public void testGetCode() {
        Response response = new Response(createMockConnection(200));
        Assert.assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetHeaders() {
        Response response = new Response(createMockConnection(200));
        Assert.assertNotNull(response.getHeaders());
    }
    
    @Test
    public void testGetHeaderExisting() {
        Response response = new Response(createMockConnection(200));
        response.getHeaders().put("Content-Type", "application/json");
        Assert.assertEquals("application/json", response.getHeader("Content-Type"));
    }
    
    @Test
    public void testGetHeaderNonExisting() {
        Response response = new Response(createMockConnection(200));
        Assert.assertNull(response.getHeader("Content-Type"));
    }
    
    private HttpURLConnection createMockConnection(int responseCode) {
        return new HttpURLConnectionMock(responseCode);
    }
    
    private class HttpURLConnectionMock extends HttpURLConnection {
        private int responseCode;
        
        protected HttpURLConnectionMock(int responseCode) {
            super(null);
            this.responseCode = responseCode;
        }
        
        @Override
        public void connect() throws IOException {
            // Do nothing
        }
        
        @Override
        public int getResponseCode() throws IOException {
            return responseCode;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }
        
        @Override
        public InputStream getErrorStream() {
            return null;
        }
        
        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> headerFields = new HashMap<>();
            List<String> contentType = new ArrayList<>();
            contentType.add("application/json");
            headerFields.put("Content-Type", contentType);
            return headerFields;
        }
    }
}