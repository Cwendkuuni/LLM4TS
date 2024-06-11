import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String testString = "test";
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
        URI testURI = URI.create("http://www.test.com");
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
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }
}