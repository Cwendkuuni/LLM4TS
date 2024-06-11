import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {
    
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
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
        URI uri = URI.create("https://www.example.com");
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
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file.getAbsolutePath(), result.toString());
    }
    
    @Test
    public void testToStringString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        
        assertEquals(string, result.toString());
    }
    
    @Test
    public void testToStringDouble() {
        Double value = 3.14;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value.toString(), result.toString());
    }
    
    @Test
    public void testToStringBoolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value.toString(), result.toString());
    }
    
    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date.toString(), result.toString());
    }
    
    @Test
    public void testToStringURI() {
        URI uri = URI.create("https://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri.toString(), result.toString());
    }
}