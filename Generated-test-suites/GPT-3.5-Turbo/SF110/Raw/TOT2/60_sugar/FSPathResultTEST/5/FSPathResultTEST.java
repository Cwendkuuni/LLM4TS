import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        String str = "test string";
        FSPathResult fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        Double num = 10.5;
        FSPathResult fsPathResult = new FSPathResult(num);
        assertEquals(num, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        assertEquals(bool, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringString() {
        String str = "test string";
        FSPathResult fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.toString());
    }

    @Test
    public void testToStringDouble() {
        Double num = 10.5;
        FSPathResult fsPathResult = new FSPathResult(num);
        assertEquals(num.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        assertEquals(bool.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}