import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Test typical case
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        // Test typical case
        String str = "Hello";
        FSPathResult fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        // Test typical case
        Double num = 10.5;
        FSPathResult fsPathResult = new FSPathResult(num);
        assertEquals(num, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        // Test typical case
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        // Test typical case
        URI uri = URI.create("https://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Test typical case
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        assertEquals(bool, fsPathResult.getBoolean());
    }

    @Test
    public void testToString() {
        // Test for File object
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
        
        // Test for String object
        String str = "Hello";
        fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.toString());
        
        // Test for Double object
        Double num = 10.5;
        fsPathResult = new FSPathResult(num);
        assertEquals(num.toString(), fsPathResult.toString());
        
        // Test for Boolean object
        Boolean bool = true;
        fsPathResult = new FSPathResult(bool);
        assertEquals(bool.toString(), fsPathResult.toString());
        
        // Test for Date object
        Date date = new Date();
        fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
        
        // Test for URI object
        URI uri = URI.create("https://www.example.com");
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }
}