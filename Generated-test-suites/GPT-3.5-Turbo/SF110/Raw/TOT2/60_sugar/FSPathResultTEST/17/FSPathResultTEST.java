import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", fsPathResult.getFile().getName());
    }

    @Test
    public void testGetString() {
        FSPathResult fsPathResult = new FSPathResult("Hello");
        assertEquals("Hello", fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult fsPathResult = new FSPathResult(10.5);
        assertEquals(10.5, fsPathResult.getDouble(), 0.0);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        assertTrue(fsPathResult.getBoolean());
    }

    @Test
    public void testToString_File() {
        FSPathResult fsPathResult = new FSPathResult(new File("test.txt"));
        assertEquals("test.txt", fsPathResult.toString());
    }

    @Test
    public void testToString_String() {
        FSPathResult fsPathResult = new FSPathResult("Hello");
        assertEquals("Hello", fsPathResult.toString());
    }

    @Test
    public void testToString_Double() {
        FSPathResult fsPathResult = new FSPathResult(10.5);
        assertEquals("10.5", fsPathResult.toString());
    }

    @Test
    public void testToString_Boolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        assertEquals("true", fsPathResult.toString());
    }

    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_URI() {
        URI uri = URI.create("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }
}