import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        // Typical use case
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        // Typical use case
        String str = "Test String";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.getString());
    }

    @Test
    public void testGetDouble() {
        // Typical use case
        Double number = 10.5;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number, result.getDouble());
    }

    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testGetURI() {
        // Typical use case
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testGetBoolean() {
        // Typical use case
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToString() {
        // Edge case when resultFile is null
        FSPathResult result = new FSPathResult((File) null);
        assertEquals("FSPathResult : empty", result.toString());
    }

}