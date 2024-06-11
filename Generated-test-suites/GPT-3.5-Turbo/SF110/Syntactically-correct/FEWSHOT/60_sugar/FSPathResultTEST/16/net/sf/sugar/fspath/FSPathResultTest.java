
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
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(number, result.getDouble());
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
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(bool, result.getBoolean());
    }
    
    @Test
    public void testToStringWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        
        String expected = file.getAbsolutePath();
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        
        String expected = string;
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        
        String expected = number.toString();
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        
        String expected = bool.toString();
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        String expected = date.toString();
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        
        String expected = uri.toString();
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult(null);
        
        String expected = "FSPathResult : empty";
        String actual = result.toString();
        
        assertEquals(expected, actual);
    }
}
