import static org.junit.Assert.*;
import org.junit.Test;
import org.scribe.model.Response;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(getDummyHttpURLConnection());
        String expected = "DummyBody";
        assertEquals(expected, response.getBody());
    }

    @Test
    public void testGetStream() {
        Response response = new Response(getDummyHttpURLConnection());
        InputStream expected = new ByteArrayInputStream("DummyStream".getBytes());
        InputStream actual = response.getStream();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCode() {
        Response response = new Response(getDummyHttpURLConnection());
        int expected = 200;
        int actual = response.getCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetHeaders() {
        Response response = new Response(getDummyHttpURLConnection());
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("Content-Type", "application/json");
        Map<String, String> actual = response.getHeaders();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetHeader() {
        Response response = new Response(getDummyHttpURLConnection());
        String expected = "application/json";
        String actual = response.getHeader("Content-Type");
        assertEquals(expected, actual);
    }

    private HttpURLConnection getDummyHttpURLConnection() {
        return new DummyHttpURLConnection();
    }

    private class DummyHttpURLConnection extends HttpURLConnection {

        DummyHttpURLConnection() {
            super(null);
        }

        @Override
        public void disconnect() { }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() { }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream("DummyStream".getBytes());
        }

        @Override
        public InputStream getErrorStream() {
            return new ByteArrayInputStream("DummyErrorStream".getBytes());
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getHeaderFields() {
            Map<String, List<String>> headers = new HashMap<String, List<String>>();
            List<String> contentTypeList = new ArrayList<String>();
            contentTypeList.add("application/json");
            headers.put("Content-Type", contentTypeList);
            return headers;
        }
    }
}