package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathResultTest {
    @Test
    public void testConstructorWithFile() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testConstructorWithString() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testConstructorWithDouble() {
        Double testDouble = 1.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    @Test
    public void testConstructorWithDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    @Test
    public void testConstructorWithURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testConstructorWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File testFile = new File("test.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String testString = "test";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double testDouble = 1.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI testURI = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }
}