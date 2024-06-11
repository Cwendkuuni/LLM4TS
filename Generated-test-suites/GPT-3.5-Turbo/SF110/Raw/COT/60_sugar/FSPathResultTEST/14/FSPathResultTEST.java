import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        
        assertEquals(testFile, result.getFile());
    }
    
    @Test
    public void testGetString() {
        String testString = "Hello World";
        FSPathResult result = new FSPathResult(testString);
        
        assertEquals(testString, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double testDouble = 4.2;
        FSPathResult result = new FSPathResult(testDouble);
        
        assertEquals(testDouble, result.getDouble());
    }
    
    @Test
    public void testGetDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        
        assertEquals(testDate, result.getDate());
    }
    
    @Test
    public void testGetURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        
        assertEquals(testURI, result.getURI());
    }
    
    @Test
    public void testGetBoolean() {
        Boolean testBoolean = Boolean.TRUE;
        FSPathResult result = new FSPathResult(testBoolean);
        
        assertEquals(testBoolean, result.getBoolean());
    }
    
    @Test
    public void testToStringWithFile() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }
    
    @Test
    public void testToStringWithString() {
        String testString = "Hello World";
        FSPathResult result = new FSPathResult(testString);
        
        assertEquals(testString, result.toString());
    }
    
    @Test
    public void testToStringWithDouble() {
        Double testDouble = 4.2;
        FSPathResult result = new FSPathResult(testDouble);
        
        assertEquals(testDouble.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithBoolean() {
        Boolean testBoolean = Boolean.TRUE;
        FSPathResult result = new FSPathResult(testBoolean);
        
        assertEquals(testBoolean.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        
        assertEquals(testDate.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        
        assertEquals(testURI.toString(), result.toString());
    }
    
    // Edge case tests and exception handling scenarios can be added here
    
}