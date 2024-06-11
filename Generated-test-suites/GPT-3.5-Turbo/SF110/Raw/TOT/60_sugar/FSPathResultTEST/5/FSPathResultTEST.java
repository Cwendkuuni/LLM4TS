import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

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
        Double number = 3.14159;
        FSPathResult result = new FSPathResult(number);
        assertEquals(number, result.getDouble());
    }

    @Test
    public void testGetDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
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
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());

        String string = "testString";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());

        Double number = 3.14159;
        result = new FSPathResult(number);
        assertEquals(number.toString(), result.toString());

        Boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());

        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());

        assertNull(new FSPathResult(null).toString());
    }

}