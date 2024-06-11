import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical use case
        FSPathResult result = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", result.getFile().getName());
        
        // Edge case
        FSPathResult result2 = new FSPathResult(new File(""));
        assertEquals("", result2.getFile().getName());
        
        // Error scenario
        FSPathResult result3 = new FSPathResult(null);
        assertNull(result3.getFile());
    }

    @Test
    public void testGetString() {
        // Typical use case
        FSPathResult result = new FSPathResult("Hello");
        assertEquals("Hello", result.getString());
        
        // Edge case
        FSPathResult result2 = new FSPathResult("");
        assertEquals("", result2.getString());
        
        // Error scenario
        FSPathResult result3 = new FSPathResult(null);
        assertNull(result3.getString());
    }

    @Test
    public void testGetDouble() {
        // Typical use case
        FSPathResult result = new FSPathResult(3.14);
        assertEquals(3.14, result.getDouble(), 0.001);
        
        // Edge case
        FSPathResult result2 = new FSPathResult(0.0);
        assertEquals(0.0, result2.getDouble(), 0.001);
        
        // Error scenario
        FSPathResult result3 = new FSPathResult(null);
        assertNull(result3.getDouble());
    }

    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
        
        // Error scenario
        FSPathResult result2 = new FSPathResult(null);
        assertNull(result2.getDate());
    }

    @Test
    public void testGetURI() {
        // Typical use case
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
        
        // Error scenario
        FSPathResult result2 = new FSPathResult(null);
        assertNull(result2.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Typical use case
        FSPathResult result = new FSPathResult(true);
        assertEquals(true, result.getBoolean());
        
        // Error scenario
        FSPathResult result2 = new FSPathResult(null);
        assertNull(result2.getBoolean());
    }

    @Test
    public void testToString() {
        FSPathResult result1 = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", result1.toString());
        
        FSPathResult result2 = new FSPathResult("Hello");
        assertEquals("Hello", result2.toString());
        
        FSPathResult result3 = new FSPathResult(3.14);
        assertEquals("3.14", result3.toString());
        
        FSPathResult result4 = new FSPathResult(true);
        assertEquals("true", result4.toString());
        
        Date date = new Date();
        FSPathResult result5 = new FSPathResult(date);
        assertEquals(date.toString(), result5.toString());
        
        URI uri = URI.create("http://www.example.com");
        FSPathResult result6 = new FSPathResult(uri);
        assertEquals(uri.toString(), result6.toString());
        
        FSPathResult result7 = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result7.toString());
    }

}