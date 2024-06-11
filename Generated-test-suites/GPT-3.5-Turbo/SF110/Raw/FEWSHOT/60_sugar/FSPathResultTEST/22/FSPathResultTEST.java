package net.sf.sugar.fspath;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathResultTest {
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }
    
    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult("test");
        assertEquals("test", result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double d = 3.14;
        FSPathResult result = new FSPathResult(d);
        assertEquals(d, result.getDouble());
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
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        
        result = new FSPathResult("test");
        assertEquals("test", result.toString());
        
        Double d = 3.14;
        result = new FSPathResult(d);
        assertEquals(d.toString(), result.toString());
        
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