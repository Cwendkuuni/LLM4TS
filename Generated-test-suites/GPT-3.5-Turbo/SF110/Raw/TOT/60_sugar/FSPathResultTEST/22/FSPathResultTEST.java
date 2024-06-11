import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical use case
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());

        // Edge case
        fsPathResult = new FSPathResult((File) null);
        assertNull(fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        // Typical use case
        String string = "Test String";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());

        // Edge case
        fsPathResult = new FSPathResult((String) null);
        assertNull(fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        // Typical use case
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue, fsPathResult.getDouble());

        // Edge case
        fsPathResult = new FSPathResult((Double) null);
        assertNull(fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());

        // Edge case
        fsPathResult = new FSPathResult((Date) null);
        assertNull(fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        // Typical use case
        URI uri = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());

        // Edge case
        fsPathResult = new FSPathResult((URI) null);
        assertNull(fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Typical use case
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals(booleanValue, fsPathResult.getBoolean());

        // Edge case
        fsPathResult = new FSPathResult((Boolean) null);
        assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testToString() {
        // Typical use case
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());

        // Edge cases for each type
        fsPathResult = new FSPathResult((File) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());

        fsPathResult = new FSPathResult((String) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());

        fsPathResult = new FSPathResult((Double) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());

        fsPathResult = new FSPathResult((Boolean) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());

        fsPathResult = new FSPathResult((Date) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());

        fsPathResult = new FSPathResult((URI) null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}