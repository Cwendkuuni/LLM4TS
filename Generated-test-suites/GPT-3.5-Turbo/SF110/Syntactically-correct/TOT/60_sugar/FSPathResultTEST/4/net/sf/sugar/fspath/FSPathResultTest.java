import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testFileConstructor() {
        File file = new File("/path/to/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testStringConstructor() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testDoubleConstructor() {
        Double d = 3.14;
        FSPathResult result = new FSPathResult(d);
        assertEquals(d, result.getDouble());
    }

    @Test
    public void testDateConstructor() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testURIConstructor() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testBooleanConstructor() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("/path/to/file.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double d = 3.14;
        FSPathResult result = new FSPathResult(d);
        String expected = d.toString();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        String expected = bool.toString();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();
        assertEquals(expected, result.toString());
    }
}