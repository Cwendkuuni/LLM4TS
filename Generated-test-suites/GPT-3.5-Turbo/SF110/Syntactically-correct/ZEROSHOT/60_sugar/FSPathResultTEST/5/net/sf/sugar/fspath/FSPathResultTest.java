import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test_file");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }
    
    @Test
    public void testGetString() {
        String string = "test_string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double value = 1.0;
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
        URI uri = URI.create("test_uri");
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
    public void testToStringWithFile() {
        File file = new File("test_file");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }
    
    @Test
    public void testToStringWithString() {
        String string = "test_string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }
    
    @Test
    public void testToStringWithDouble() {
        Double value = 1.0;
        FSPathResult result = new FSPathResult(value);
        assertEquals(value.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("test_uri");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }
    
    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}