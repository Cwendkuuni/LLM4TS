import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    private Response response;
    private HttpURLConnection mockConnection;
    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;

    @Before
    public void setup() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream("Test".getBytes());
        mockConnection = new HttpURLConnectionMock();
        response = new Response(mockConnection);
    }

    @Test
    public void testGetBody() {
        String body = response.getBody();
        
        Assert.assertEquals("Test", body);
    }

    @Test
    public void testGetStream() {
        InputStream stream = response.getStream();

        Assert.assertEquals(inputStream, stream);
    }

    @Test
    public void testGetCode() {
        int code = response.getCode();

        Assert.assertEquals(200, code);
    }

    @Test
    public void testGetHeaders() {
        Map<String, String> headers = response.getHeaders();

        Assert.assertEquals(new HashMap<>(), headers);
    }

    @Test
    public void testGetHeader() {
        String header = response.getHeader("TestHeader");

        Assert.assertNull(header);
    }

    private class HttpURLConnectionMock extends HttpURLConnection {

        protected HttpURLConnectionMock() {
            super(null);
        }

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
        public int getResponseCode() throws IOException {
            return 200;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public InputStream getErrorStream() {
            return null;
        }

        @Override
        public Map<String, String> getHeaderFields() {
            return new HashMap<>();
        }
    }
}