import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    private File testFile = new File("testFile.txt");
    private String testString = "testString";
    private Double testDouble = 3.14;
    private Date testDate = new Date();
    private URI testURI = URI.create("https://www.example.com");
    private Boolean testBoolean = true;

    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    @Test
    public void testGetDate() {
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    @Test
    public void testGetURI() {
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }

    @Test
    public void testToStringDouble() {
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), result.toString());
    }

    @Test
    public void testToStringDate() {
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}

