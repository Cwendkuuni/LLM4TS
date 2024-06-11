import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Arrange
        Response response = new Response(null);
        
        // Act
        String body = response.getBody();
        
        // Assert
        assertNull(body);
    }
    
    @Test
    public void testGetStream() {
        // Arrange
        Response response = new Response(null);
        
        // Act
        InputStream stream = response.getStream();
        
        // Assert
        assertNull(stream);
    }
    
    @Test
    public void testGetCode() {
        // Arrange
        Response response = new Response(null);
        
        // Act
        int code = response.getCode();
        
        // Assert
        assertNotEquals(0, code);
    }
    
    @Test
    public void testGetHeaders() {
        // Arrange
        Response response = new Response(null);
        
        // Act
        Map<String, String> headers = response.getHeaders();
        
        // Assert
        assertNotNull(headers);
        assertEquals(0, headers.size());
    }
    
    @Test
    public void testGetHeader() {
        // Arrange
        Response response = new Response(null);
        
        // Act
        String header = response.getHeader("name");
        
        // Assert
        assertNull(header);
    }
}