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
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double number = 1.5;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number, result.getDouble());
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
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }
    
    @Test
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        
        String string = "test";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());
        
        Double number = 1.5;
        result = new FSPathResult(number);
        assertEquals(number.toString(), result.toString());
        
        Boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
        
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