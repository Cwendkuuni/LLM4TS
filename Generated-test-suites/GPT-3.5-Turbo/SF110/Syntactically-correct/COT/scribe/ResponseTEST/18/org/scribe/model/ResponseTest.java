
package org.scribe.model;

import org.junit.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = createResponse();
        String body = response.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals("", body);
    }

    @Test
    public void testGetStream() {
        Response response = createResponse();
        Assert.assertNotNull(response.getStream());
    }

    @Test
    public void testGetCode() {
        Response response = createResponse();
        Assert.assertEquals(404, response.getCode());
    }

    @Test
    public void testGetHeaders() {
        Response response = createResponse();
        Map<String, String> headers = response.getHeaders();
        Assert.assertNotNull(headers);
    }

    @Test
    public void testGetHeader() {
        Response response = createResponse();
        String header = response.getHeader("name");
        Assert.assertNull(header);
    }

    private Response createResponse() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("http://example.com");
            connection = (HttpURLConnection) url.openConnection();
            return new Response(connection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
