import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        String testString = "Test String";
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        Double testDouble = 10.5;
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean testBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringString() {
        String testString = "Test String";
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.toString());
    }

    @Test
    public void testToStringDouble() {
        Double testDouble = 10.5;
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean testBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringDate() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), fsPathResult.toString());
    }
}