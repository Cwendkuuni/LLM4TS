package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testConstructorWithFile() {
        File file = new File("test.txt");
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
        String string = "test";
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
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(value, result.getDouble());
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
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(value, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        assertEquals(value.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertEquals(value.toString(), result.toString());
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
        FSPathResult result = new FSPathResult((File) null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}