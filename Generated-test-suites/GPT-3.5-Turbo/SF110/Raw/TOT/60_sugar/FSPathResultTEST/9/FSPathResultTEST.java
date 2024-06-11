import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertNotNull(result.getString());
        assertEquals(string, result.getString());
    }

    @Test
    public void testGetDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        assertNotNull(result.getDouble());
        assertEquals(number, result.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNotNull(result.getDate());
        assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("https://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNotNull(result.getURI());
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertNotNull(result.getBoolean());
        assertEquals(value, result.getBoolean());
    }

    @Test
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithNullResult() {
        FSPathResult result = new FSPathResult((File) null);
        String expected = "FSPathResult : empty";
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithMultipleResults() {
        FSPathResult result = new FSPathResult((File) null);
        result = new FSPathResult("test");
        result = new FSPathResult(3.14);
        result = new FSPathResult(true);
        result = new FSPathResult(new Date());
        result = new FSPathResult(URI.create("https://example.com"));
        String expected = "FSPathResult : empty";
        assertEquals(expected, result.toString());
    }
}