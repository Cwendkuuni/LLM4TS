import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    // Test constructor FSPathResult(File file)
    @Test
    public void testConstructorFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result);
        assertEquals(file, result.getFile());
    }

    // Test constructor FSPathResult(String string)
    @Test
    public void testConstructorString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertNotNull(result);
        assertEquals(string, result.getString());
    }

    // Test constructor FSPathResult(Double resultDouble)
    @Test
    public void testConstructorDouble() {
        Double resultDouble = 0.5;
        FSPathResult result = new FSPathResult(resultDouble);
        assertNotNull(result);
        assertEquals(resultDouble, result.getDouble());
    }

    // Test constructor FSPathResult(Date date)
    @Test
    public void testConstructorDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNotNull(result);
        assertEquals(date, result.getDate());
    }

    // Test constructor FSPathResult(URI uri)
    @Test
    public void testConstructorURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNotNull(result);
        assertEquals(uri, result.getURI());
    }

    // Test constructor FSPathResult(Boolean resultBoolean)
    @Test
    public void testConstructorBoolean() {
        Boolean resultBoolean = true;
        FSPathResult result = new FSPathResult(resultBoolean);
        assertNotNull(result);
        assertEquals(resultBoolean, result.getBoolean());
    }

    // Test toString() method
    @Test
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = result.toString();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}