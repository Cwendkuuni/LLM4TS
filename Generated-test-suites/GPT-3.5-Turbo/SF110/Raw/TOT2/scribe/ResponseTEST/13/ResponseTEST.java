
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResponseTest {

    @Test
    public void testResponseConstructor() {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(200);
    
        Response response = new Response(connection);
    
        Assert.assertEquals(200, response.getCode());
    }
    
    @Test
    public void testGetBody() {
        Response response = new Response(mock(HttpURLConnection.class));
        
        String expectedBody = "Test body";
        response.getBody(); // Trigger parseBodyContents()
        Assert.assertEquals(expectedBody, response.getBody());
    }
    
    @Test
    public void testGetHeader() {
        Response response = new Response(mock(HttpURLConnection.class));
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        when(connection.getHeaderFields()).thenReturn(headers);
        
        String headerValue = response.getHeader("Content-Type");
        
        Assert.assertEquals("application/json", headerValue);
    }
}
