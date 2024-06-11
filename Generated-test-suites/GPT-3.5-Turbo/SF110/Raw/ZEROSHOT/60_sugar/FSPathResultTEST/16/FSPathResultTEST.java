import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        File file = result.getFile();
        assertEquals("test.txt", file.getName());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult("test");
        String string = result.getString();
        assertEquals("test", string);
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(1.23);
        Double value = result.getDouble();
        assertEquals(1.23, value, 0.01);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date value = result.getDate();
        assertEquals(date, value);
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        URI value = result.getURI();
        assertEquals(uri, value);
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(true);
        Boolean value = result.getBoolean();
        assertTrue(value);
    }

    @Test
    public void testToString() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        String str = result.toString();
        assertEquals("test.txt", str);
    }
}