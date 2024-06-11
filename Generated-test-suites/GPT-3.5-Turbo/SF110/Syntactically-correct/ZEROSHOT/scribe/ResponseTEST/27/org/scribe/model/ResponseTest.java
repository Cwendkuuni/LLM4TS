import org.junit.*;
import java.net.*;
import java.io.*;
import org.scribe.model.*;
import org.scribe.utils.*;
import java.util.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        Response response = new Response(null);
        
        String expected = "";
        String actual = response.getBody();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetBody_withActualBody() {
        InputStream inputStream = new ByteArrayInputStream("Test Body".getBytes());
        Response response = new Response(null);
        response.setStream(inputStream);
        
        String expected = "Test Body";
        String actual = response.getBody();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetStream() {
        InputStream expected = null;
        Response response = new Response(null);
        
        InputStream actual = response.getStream();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetCode() {
        int expected = 0;
        Response response = new Response(null);
        
        int actual = response.getCode();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetHeaders() {
        Map<String, String> expected = new HashMap<String, String>();
        Response response = new Response(null);
        
        Map<String, String> actual = response.getHeaders();
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetHeader() {
        String expected = null;
        Response response = new Response(null);
        
        String actual = response.getHeader("name");
        
        Assert.assertEquals(expected, actual);
    }
}