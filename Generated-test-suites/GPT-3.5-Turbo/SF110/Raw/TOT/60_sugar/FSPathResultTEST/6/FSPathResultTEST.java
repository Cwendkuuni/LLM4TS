import org.junit.Test;
import java.io.File;
import java.util.Date;
import java.net.URI;
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
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        
        assertEquals(string, result.getString());
    }

    @Test
    public void testGetDouble() {
        double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        
        assertEquals(number, result.getDouble(), 0.001);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        boolean value = true;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(value, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        
        assertEquals(Double.toString(number), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        boolean value = true;
        FSPathResult result = new FSPathResult(value);
        
        assertEquals(Boolean.toString(value), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        
        assertEquals(uri.toString(), result.toString());
    }
}