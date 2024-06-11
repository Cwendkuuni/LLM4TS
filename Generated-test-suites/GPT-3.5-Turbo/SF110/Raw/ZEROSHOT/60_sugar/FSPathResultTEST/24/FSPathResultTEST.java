
import static org.junit.Assert.*;
import org.junit.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        assertEquals(new File("test.txt"), result.getFile());
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
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertEquals(true, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", result.toString());
    }

    @Test
    public void testToStringWithString() {
        FSPathResult result = new FSPathResult("test");
        assertEquals("test", result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        FSPathResult result = new FSPathResult(3.14);
        assertEquals("3.14", result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertEquals("true", result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

}
