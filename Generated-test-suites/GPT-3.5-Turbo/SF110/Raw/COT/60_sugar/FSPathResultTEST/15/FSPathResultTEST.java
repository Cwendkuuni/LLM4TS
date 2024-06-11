
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        String testString = "test";
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
    public void testToString_File() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToString_String() {
        String testString = "test";
        FSPathResult fsPathResult = new FSPathResult(testString);
        assertEquals(testString, fsPathResult.toString());
    }

    @Test
    public void testToString_Double() {
        Double testDouble = 10.5;
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Boolean() {
        Boolean testBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Date() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_URI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_EmptyResult() {
        FSPathResult fsPathResult = new FSPathResult((File) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}
