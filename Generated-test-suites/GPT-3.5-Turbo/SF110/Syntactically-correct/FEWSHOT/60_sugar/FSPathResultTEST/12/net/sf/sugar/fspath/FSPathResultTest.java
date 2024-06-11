import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testConstructorWithFile() {
        File file = new File("testfile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertNull(result.getFile());
        assertEquals(string, result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(doubleValue, result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertEquals(date, result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertEquals(uri, result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testfile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}