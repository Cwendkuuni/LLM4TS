import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        String string = "test string";
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
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
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
        assertEquals(uri.toString(), result.toString());
    }
}