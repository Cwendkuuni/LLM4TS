import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testGetString() {
        String testString = "Test String";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testGetDouble() {
        Double testDouble = 10.5;
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
        URI testURI = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToString_File() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testToString_String() {
        String testString = "Test String";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }

    @Test
    public void testToString_Double() {
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals("10.5", result.toString());
    }

    @Test
    public void testToString_Boolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals("true", result.toString());
    }

    @Test
    public void testToString_Date() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testToString_URI() {
        URI testURI = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals("http://example.com", result.toString());
    }
}