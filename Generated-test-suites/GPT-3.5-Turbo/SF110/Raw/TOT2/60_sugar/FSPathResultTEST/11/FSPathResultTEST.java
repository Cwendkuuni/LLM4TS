import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testfile.txt");
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
        Double dbl = 10.5;
        FSPathResult fsPathResult = new FSPathResult(dbl);
        assertEquals(dbl, fsPathResult.getDouble());
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
    public void testToString_File() {
        File file = new File("testfile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToString_String() {
        String str = "test string";
        FSPathResult fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.toString());
    }

    @Test
    public void testToString_Double() {
        Double dbl = 10.5;
        FSPathResult fsPathResult = new FSPathResult(dbl);
        assertEquals(dbl.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Boolean() {
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        assertEquals(bool.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToString_URI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }
}