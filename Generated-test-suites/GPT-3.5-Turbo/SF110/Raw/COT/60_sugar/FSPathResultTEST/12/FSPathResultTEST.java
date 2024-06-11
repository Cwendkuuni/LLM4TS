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
        FSPathResult result = new FSPathResult("Test String");
        String string = result.getString();
        assertEquals("Test String", string);
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(3.14);
        double value = result.getDouble();
        assertEquals(3.14, value, 0.001);
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
        URI uri = URI.create("https://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        URI value = result.getURI();
        assertEquals(uri, value);
    }

    @Test
    public void testGetBoolean() {
        FSPathResult result = new FSPathResult(true);
        boolean value = result.getBoolean();
        assertTrue(value);
    }

    @Test
    public void testToString() {
        FSPathResult result = new FSPathResult(new File("test.txt"));
        String value = result.toString();
        assertEquals("test.txt", value);
    }
}