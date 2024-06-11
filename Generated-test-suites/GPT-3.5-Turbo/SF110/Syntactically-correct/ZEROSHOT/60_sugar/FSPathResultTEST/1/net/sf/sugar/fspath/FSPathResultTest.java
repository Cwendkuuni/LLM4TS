import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", result.getFile().getName());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult("test");
        assertEquals("test", result.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(3.14);
        assertEquals(3.14, result.getDouble(), 0.01);
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
        FSPathResult result = new FSPathResult(true);
        assertEquals(true, result.getBoolean());
    }

    @Test
    public void testToString() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", result.toString());
    }

}