import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    // Test case for constructor with File parameter
    @Test
    public void testFileConstructor() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    // Test case for constructor with String parameter
    @Test
    public void testStringConstructor() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    // Test case for constructor with Double parameter
    @Test
    public void testDoubleConstructor() {
        Double testDouble = 1.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    // Test case for constructor with Date parameter
    @Test
    public void testDateConstructor() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    // Test case for constructor with URI parameter
    @Test
    public void testURIConstructor() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    // Test case for constructor with Boolean parameter
    @Test
    public void testBooleanConstructor() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    // Test case for toString method with File object
    @Test
    public void testToStringWithFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }

    // Test case for toString method with String object
    @Test
    public void testToStringWithString() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }

    // Test case for toString method with Double object
    @Test
    public void testToStringWithDouble() {
        Double testDouble = 1.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), result.toString());
    }

    // Test case for toString method with Boolean object
    @Test
    public void testToStringWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), result.toString());
    }

    // Test case for toString method with Date object
    @Test
    public void testToStringWithDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    // Test case for toString method with URI object
    @Test
    public void testToStringWithURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }
}