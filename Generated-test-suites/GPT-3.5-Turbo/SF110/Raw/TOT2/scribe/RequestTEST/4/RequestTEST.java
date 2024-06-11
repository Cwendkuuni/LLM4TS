import org.junit.Test;

public class RequestTest {

    @Test
    public void testRequestConstructor() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        assertNotNull(request);
        assertEquals(Verb.GET, request.getVerb());
        assertEquals("http://www.example.com", request.getUrl());
    }

    @Test
    public void testAddHeader() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addHeader("Content-Type", "application/json");
        assertEquals("application/json", request.getHeaders().get("Content-Type"));
    }

    @Test
    public void testAddBodyParameter() {
        Request request = new Request(Verb.POST, "http://www.example.com");
        request.addBodyParameter("key", "value");
        assertEquals("value", request.getBodyParams().get("key"));
    }

    @Test
    public void testAddQuerystringParameter() {
        Request request = new Request(Verb.GET, "http://www.example.com");
        request.addQuerystringParameter("param", "value");
        assertEquals("value", request.getQueryStringParams().get("param"));
    }

    // Add more test cases for the remaining methods

}