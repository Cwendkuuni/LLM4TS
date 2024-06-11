import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFileConstructor() {
        File testFile = new File("test.txt");
        FSPathResult pathResult = new FSPathResult(testFile);
        assertEquals(testFile, pathResult.getFile());
    }

    @Test
    public void testStringConstructor() {
        String testString = "test";
        FSPathResult pathResult = new FSPathResult(testString);
        assertEquals(testString, pathResult.getString());
    }

    @Test
    public void testDoubleConstructor() {
        Double testDouble = 10.5;
        FSPathResult pathResult = new FSPathResult(testDouble);
        assertEquals(testDouble, pathResult.getDouble());
    }

    @Test
    public void testDateConstructor() {
        Date testDate = new Date();
        FSPathResult pathResult = new FSPathResult(testDate);
        assertEquals(testDate, pathResult.getDate());
    }

    @Test
    public void testURIConstructor() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult pathResult = new FSPathResult(testURI);
        assertEquals(testURI, pathResult.getURI());
    }

    @Test
    public void testBooleanConstructor() {
        Boolean testBoolean = true;
        FSPathResult pathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean, pathResult.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File testFile = new File("test.txt");
        FSPathResult pathResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), pathResult.toString());
    }

    @Test
    public void testToStringWithString() {
        String testString = "test";
        FSPathResult pathResult = new FSPathResult(testString);
        assertEquals(testString, pathResult.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double testDouble = 10.5;
        FSPathResult pathResult = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), pathResult.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult pathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), pathResult.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date testDate = new Date();
        FSPathResult pathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), pathResult.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult pathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), pathResult.toString());
    }
}