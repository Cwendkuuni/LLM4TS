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
        Double d = 1.0;
        FSPathResult result = new FSPathResult(d);
        assertEquals(d, result.getDouble());
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
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("test.txt", result.toString());
    }

    @Test
    public void testToStringString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals("test", result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double d = 1.0;
        FSPathResult result = new FSPathResult(d);
        assertEquals("1.0", result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals("true", result.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals("http://www.example.com", result.toString());
    }
}