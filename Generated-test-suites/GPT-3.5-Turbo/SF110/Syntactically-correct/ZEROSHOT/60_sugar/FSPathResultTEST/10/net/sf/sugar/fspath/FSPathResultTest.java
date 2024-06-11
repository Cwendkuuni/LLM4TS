import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file, result.getFile());
    }
    
    @Test
    public void testGetString() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);
        
        assertEquals(str, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value, result.getDouble());
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
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value, result.getBoolean());
    }
    
    @Test
    public void testToString_File() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file.getAbsolutePath(), result.toString());
    }
    
    @Test
    public void testToString_String() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);
        
        assertEquals(str, result.toString());
    }
    
    @Test
    public void testToString_Double() {
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value.toString(), result.toString());
    }
    
    @Test
    public void testToString_Boolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value.toString(), result.toString());
    }
    
    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date.toString(), result.toString());
    }
    
    @Test
    public void testToString_URI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri.toString(), result.toString());
    }
    
    @Test
    public void testToString_Empty() {
        FSPathResult result = new FSPathResult(null);
        
        assertEquals("FSPathResult : empty", result.toString());
    }
}