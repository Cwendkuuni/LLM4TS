import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical use case: result file is not null
        File file = new File("testfile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
        
        // Edge case: result file is null
        result = new FSPathResult((File) null);
        assertNull(result.getFile());
    }
    
    @Test
    public void testGetString() {
        // Typical use case: result string is not null
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
        
        // Edge case: result string is null
        result = new FSPathResult((String) null);
        assertNull(result.getString());
    }
    
    @Test
    public void testGetDouble() {
        // Typical use case: result double is not null
        Double doub = 3.14;
        FSPathResult result = new FSPathResult(doub);
        assertEquals(doub, result.getDouble());
        
        // Edge case: result double is null
        result = new FSPathResult((Double) null);
        assertNull(result.getDouble());
    }
    
    @Test
    public void testGetDate() {
        // Typical use case: result date is not null
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
        
        // Edge case: result date is null
        result = new FSPathResult((Date) null);
        assertNull(result.getDate());
    }
    
    @Test
    public void testGetURI() {
        // Typical use case: result URI is not null
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
        
        // Edge case: result URI is null
        result = new FSPathResult((URI) null);
        assertNull(result.getURI());
    }
    
    @Test
    public void testGetBoolean() {
        // Typical use case: result boolean is not null
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
        
        // Edge case: result boolean is null
        result = new FSPathResult((Boolean) null);
        assertNull(result.getBoolean());
    }
    
    @Test
    public void testToString() {
        // Typical use case: result is a file
        File file = new File("testfile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        
        // Typical use case: result is a string
        String string = "test string";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());
        
        // Typical use case: result is a double
        Double doub = 3.14;
        result = new FSPathResult(doub);
        assertEquals(doub.toString(), result.toString());
        
        // Typical use case: result is a boolean
        Boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
        
        // Typical use case: result is a date
        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
        
        // Typical use case: result is a URI
        URI uri = URI.create("http://example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
        
        // Edge case: result is null
        result = new FSPathResult((File) null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}