import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    // Test case for getFile() method
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file, result.getFile());
    }
    
    // Test case for getString() method
    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        
        assertEquals(string, result.getString());
    }
    
    // Test case for getDouble() method
    @Test
    public void testGetDouble() {
        Double number = 10.5;
        FSPathResult result = new FSPathResult(number);
        
        assertEquals(number, result.getDouble());
    }

    // Test case for getDate() method
    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date, result.getDate());
    }
    
    // Test case for getURI() method
    @Test
    public void testGetURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri, result.getURI());
    }
    
    // Test case for getBoolean() method
    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        assertEquals(bool, result.getBoolean());
    }
    
    // Test case for toString() method when underlying object is a File
    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file.getAbsolutePath(), result.toString());
    }
    
    // Test case for toString() method when underlying object is a String
    @Test
    public void testToStringWithString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        
        assertEquals(string, result.toString());
    }
    
    // Test case for toString() method when underlying object is a Double
    @Test
    public void testToStringWithDouble() {
        Double number = 10.5;
        FSPathResult result = new FSPathResult(number);
        
        assertEquals(number.toString(), result.toString());
    }
    
    // Test case for toString() method when underlying object is a Boolean
    @Test
    public void testToStringWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        assertEquals(bool.toString(), result.toString());
    }
    
    // Test case for toString() method when underlying object is a Date
    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date.toString(), result.toString());
    }
    
    // Test case for toString() method when underlying object is a URI
    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri.toString(), result.toString());
    }
    
    // Test case for toString() method when underlying object is null
    @Test
    public void testToStringWithNull() {
        FSPathResult result = new FSPathResult(null);
        
        assertEquals("FSPathResult : empty", result.toString());
    }
    
}