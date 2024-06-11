package net.sf.sugar.fspath;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult("testString");
        assertEquals("testString", result.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(12.34);
        assertEquals(12.34, result.getDouble(), 0.0);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertEquals(true, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        FSPathResult result = new FSPathResult("testString");
        assertEquals("testString", result.toString());
    }

    @Test
    public void testToStringDouble() {
        FSPathResult result = new FSPathResult(12.34);
        assertEquals("12.34", result.toString());
    }

    @Test
    public void testToStringBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertEquals("true", result.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

}