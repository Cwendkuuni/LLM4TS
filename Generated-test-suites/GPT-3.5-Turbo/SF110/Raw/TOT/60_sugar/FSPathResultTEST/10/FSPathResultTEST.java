import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    private File file = new File("test.txt");
    private String string = "test";
    private Double aDouble = 10.5;
    private Date date = new Date();
    private URI uri = URI.create("http://www.example.com");
    private Boolean aBoolean = true;

    private FSPathResult fsPathResult;

    @Test
    public void testGetFile() {
        fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        fsPathResult = new FSPathResult(aDouble);
        assertEquals(aDouble, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        fsPathResult = new FSPathResult(aBoolean);
        assertEquals(aBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithString() {
        fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testToStringWithDouble() {
        fsPathResult = new FSPathResult(aDouble);
        assertEquals(aDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        fsPathResult = new FSPathResult(aBoolean);
        assertEquals(aBoolean.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithDate() {
        fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithURI() {
        fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }
}