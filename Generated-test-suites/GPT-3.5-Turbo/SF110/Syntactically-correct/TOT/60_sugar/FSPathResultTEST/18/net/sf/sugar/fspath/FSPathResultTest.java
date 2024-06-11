import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

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
        URI uri = URI.create("http://example.com");
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
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);

        String expected = file.getAbsolutePath();
        assertEquals(expected, result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);

        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double number = 3.14;
        FSPathResult result = new FSPathResult(number);

        assertEquals(number.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);

        assertEquals(bool.toString(), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);

        assertEquals(uri.toString(), result.toString());
    }

}