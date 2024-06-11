import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    // Test case for constructor FSPathResult(File file)
    @Test
    public void testConstructorFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    // Test case for constructor FSPathResult(String string)
    @Test
    public void testConstructorString() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.getString());
    }

    // Test case for constructor FSPathResult(Double resultDouble)
    @Test
    public void testConstructorDouble() {
        Double dbl = 3.14;
        FSPathResult result = new FSPathResult(dbl);
        assertEquals(dbl, result.getDouble());
    }

    // Test case for constructor FSPathResult(Date date)
    @Test
    public void testConstructorDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    // Test case for constructor FSPathResult(URI uri)
    @Test
    public void testConstructorURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    // Test case for constructor FSPathResult(Boolean resultBoolean)
    @Test
    public void testConstructorBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool, result.getBoolean());
    }

    // Test case for toString() with underlying object as File
    @Test
    public void testToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    // Test case for toString() with underlying object as String
    @Test
    public void testToStringString() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.toString());
    }

    // Test case for toString() with underlying object as Double
    @Test
    public void testToStringDouble() {
        Double dbl = 3.14;
        FSPathResult result = new FSPathResult(dbl);
        assertEquals(dbl.toString(), result.toString());
    }

    // Test case for toString() with underlying object as Boolean
    @Test
    public void testToStringBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
    }

    // Test case for toString() with underlying object as Date
    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    // Test case for toString() with underlying object as URI
    @Test
    public void testToStringURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }
}