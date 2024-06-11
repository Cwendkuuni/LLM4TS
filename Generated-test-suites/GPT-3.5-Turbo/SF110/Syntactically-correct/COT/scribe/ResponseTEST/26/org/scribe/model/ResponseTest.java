
import org.junit.Test;
import static org.junit.Assert.*;

public class ResponseTest {

    @Test
    public void testGetBody() {
        // Arrange
        Response response = new Response();
        response.body = "This is the body";
        
        // Act
        String result = response.getBody();
        
        // Assert
        assertEquals("This is the body", result);
    }
    
    @Test
    public void testGetStream() {
        // Arrange
        Response response = new Response();
        response.stream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        
        // Act
        InputStream result = response.getStream();
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testGetCode() {
        // Arrange
        Response response = new Response();
        response.code = 200;
        
        // Act
        int result = response.getCode();
        
        // Assert
        assertEquals(200, result);
    }
    
    @Test
    public void testGetHeaders() {
        // Arrange
        Response response = new Response();
        response.headers = new HashMap<String, String>();
        response.headers.put("Content-Type", "application/json");
        
        // Act
        Map<String, String> result = response.getHeaders();
        
        // Assert
        assertNotNull(result);
        assertEquals("application/json", result.get("Content-Type"));
    }
    
    @Test
    public void testGetHeader() {
        // Arrange
        Response response = new Response();
        response.headers = new HashMap<String, String>();
        response.headers.put("Content-Type", "application/json");
        
        // Act
        String result = response.getHeader("Content-Type");
        
        // Assert
        assertEquals("application/json", result);
    }
}
