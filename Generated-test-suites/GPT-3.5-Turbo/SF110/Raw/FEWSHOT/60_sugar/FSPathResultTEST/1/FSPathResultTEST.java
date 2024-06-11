package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {
    
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
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
        FSPathResult result = new FSPathResult(3.14);
        assertEquals(Double.valueOf(3.14), result.getDouble());
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
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        
        String string = "testString";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());
        
        double value = 3.14;
        result = new FSPathResult(value);
        assertEquals(String.valueOf(value), result.toString());
        
        boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(String.valueOf(bool), result.toString());
        
        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
        
        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
        
        result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}