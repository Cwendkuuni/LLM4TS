import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical use case
        File file = new File("path/to/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getFile());
    }

    @Test
    public void testGetString() {
        // Typical use case
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getString());
    }

    @Test
    public void testGetDouble() {
        // Typical use case
        double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        assertEquals(Double.valueOf(number), result.getDouble());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getDouble());
    }

    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getDate());
    }

    @Test
    public void testGetURI() {
        // Typical use case
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Typical use case
        boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertEquals(Boolean.valueOf(value), result.getBoolean());
        
        // Edge case
        result = new FSPathResult(null);
        assertNull(result.getBoolean());
    }

    @Test
    public void testToString() {
        // Typical use case - File
        File file = new File("path/to/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        
        // Typical use case - String
        String string = "Hello World";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());
        
        // Typical use case - Double
        double number = 3.14;
        result = new FSPathResult(number);
        assertEquals(Double.toString(number), result.toString());
        
        // Typical use case - Boolean
        boolean value = true;
        result = new FSPathResult(value);
        assertEquals(Boolean.toString(value), result.toString());
        
        // Typical use case - Date
        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
        
        // Typical use case - URI
        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
        
        // Edge case
        result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}