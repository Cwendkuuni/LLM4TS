
package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
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
    public void testFSPathResultString() {
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
    public void testFSPathResultDouble() {
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
    public void testFSPathResultDate() {
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
    public void testFSPathResultURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertEquals(uri, result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultBoolean() {
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
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult resultFile = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), resultFile.toString());

        String string = "test";
        FSPathResult resultString = new FSPathResult(string);
        assertEquals(string, resultString.toString());

        Double value = 3.14;
        FSPathResult resultDouble = new FSPathResult(value);
        assertEquals(value.toString(), resultDouble.toString());

        Date date = new Date();
        FSPathResult resultDate = new FSPathResult(date);
        assertEquals(date.toString(), resultDate.toString());

        URI uri = URI.create("http://example.com");
        FSPathResult resultURI = new FSPathResult(uri);
        assertEquals(uri.toString(), resultURI.toString());

        Boolean booleanValue = true;
        FSPathResult resultBoolean = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), resultBoolean.toString());

        FSPathResult resultEmpty = new FSPathResult(null);
        assertEquals("FSPathResult : empty", resultEmpty.toString());
    }
}
