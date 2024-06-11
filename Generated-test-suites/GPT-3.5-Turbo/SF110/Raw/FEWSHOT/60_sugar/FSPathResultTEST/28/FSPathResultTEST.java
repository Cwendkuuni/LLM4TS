package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testConstructorWithFile() {
        File file = new File("/path/to/file");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testConstructorWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testConstructorWithDouble() {
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        assertEquals(value, result.getDouble());
    }

    @Test
    public void testConstructorWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testConstructorWithURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testConstructorWithBoolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertEquals(value, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("/path/to/file");
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
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}