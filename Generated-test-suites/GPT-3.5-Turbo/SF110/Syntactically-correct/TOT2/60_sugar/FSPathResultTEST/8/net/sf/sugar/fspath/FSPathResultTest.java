package net.sf.sugar.fspath;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testConstructorFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile, result.getFile());
    }

    @Test
    public void testConstructorString() {
        String testString = "testString";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.getString());
    }

    @Test
    public void testConstructorDouble() {
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble, result.getDouble());
    }

    @Test
    public void testConstructorDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate, result.getDate());
    }

    @Test
    public void testConstructorURI() {
        URI testURI = URI.create("http://www.test.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI, result.getURI());
    }

    @Test
    public void testConstructorBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        String testString = "testString";
        FSPathResult result = new FSPathResult(testString);
        assertEquals(testString, result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double testDouble = 10.5;
        FSPathResult result = new FSPathResult(testDouble);
        assertEquals(testDouble.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        assertEquals(testBoolean.toString(), result.toString());
    }

    @Test
    public void testToStringDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI testURI = URI.create("http://www.test.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }
}