import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testGetDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number, result.getDouble());
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
    public void testToStringForFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringForString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringForDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number.toString(), result.toString());
    }

    @Test
    public void testToStringForBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
    }

    @Test
    public void testToStringForDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringForURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }
}

