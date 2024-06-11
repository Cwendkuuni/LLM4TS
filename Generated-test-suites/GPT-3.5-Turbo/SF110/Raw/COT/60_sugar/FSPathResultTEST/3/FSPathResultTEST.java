import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {
    // Test getFile() method
    @Test
    public void testGetFile() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }
    
    // Test getString() method
    @Test
    public void testGetString() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }
    
    // Test getDouble() method
    @Test
    public void testGetDouble() {
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }
    
    // Test getDate() method
    @Test
    public void testGetDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }
    
    // Test getURI() method
    @Test
    public void testGetURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }
    
    // Test getBoolean() method
    @Test
    public void testGetBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }
    
    // Test toString() method - File
    @Test
    public void testToString_File() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }
    
    // Test toString() method - String
    @Test
    public void testToString_String() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }
    
    // Test toString() method - Double
    @Test
    public void testToString_Double() {
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), result.toString());
    }
    
    // Test toString() method - Boolean
    @Test
    public void testToString_Boolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), result.toString());
    }
    
    // Test toString() method - Date
    @Test
    public void testToString_Date() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }
    
    // Test toString() method - URI
    @Test
    public void testToString_URI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }
}