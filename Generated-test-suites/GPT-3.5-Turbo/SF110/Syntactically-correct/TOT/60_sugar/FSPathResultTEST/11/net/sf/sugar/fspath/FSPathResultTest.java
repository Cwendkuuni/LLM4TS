import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical case: file is provided
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        // Typical case: string is provided
        String str = "Hello World";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.getString());
    }

    @Test
    public void testGetDouble() {
        // Typical case: double is provided
        Double d = 3.14;
        FSPathResult result = new FSPathResult(d);
        assertEquals(d, result.getDouble());
    }

    @Test
    public void testGetDate() {
        // Typical case: date is provided
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        // Typical case: URI is provided
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Typical case: boolean is provided
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToString() {
        // Typical case: file object
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());

        // Typical case: string object
        String str = "Hello World";
        result = new FSPathResult(str);
        assertEquals(str, result.toString());

        // Typical case: double object
        Double d = 3.14;
        result = new FSPathResult(d);
        assertEquals(d.toString(), result.toString());

        // Typical case: boolean object
        Boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());

        // Typical case: date object
        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());

        // Typical case: URI object
        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());

        // Edge case: all fields are null
        result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testGetFile_Null() {
        // Edge case: file is null
        FSPathResult result = new FSPathResult((File)null);
        assertNull(result.getFile());
    }

    @Test
    public void testGetString_Null() {
        // Edge case: string is null
        FSPathResult result = new FSPathResult((String)null);
        assertNull(result.getString());
    }

    @Test
    public void testGetDouble_Null() {
        // Edge case: double is null
        FSPathResult result = new FSPathResult((Double)null);
        assertNull(result.getDouble());
    }

    @Test
    public void testGetDate_Null() {
        // Edge case: date is null
        FSPathResult result = new FSPathResult((Date)null);
        assertNull(result.getDate());
    }

    @Test
    public void testGetURI_Null() {
        // Edge case: URI is null
        FSPathResult result = new FSPathResult((URI)null);
        assertNull(result.getURI());
    }

    @Test
    public void testGetBoolean_Null() {
        // Edge case: boolean is null
        FSPathResult result = new FSPathResult((Boolean)null);
        assertNull(result.getBoolean());
    }

}