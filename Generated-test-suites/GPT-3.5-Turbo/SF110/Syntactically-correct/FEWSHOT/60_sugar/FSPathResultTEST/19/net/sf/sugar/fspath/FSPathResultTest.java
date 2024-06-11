
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathResultTest {
    @Test
    public void testConstructorFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertNull(result.getFile());
        assertEquals(string, result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorDouble() {
        Double d = 3.14;
        FSPathResult result = new FSPathResult(d);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(d, result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertEquals(date, result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorURI() {
        URI uri = URI.create("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertEquals(uri, result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(bool, result.getBoolean());
    }

    @Test
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());

        String string = "test";
        result = new FSPathResult(string);
        assertEquals(string, result.toString());

        Double d = 3.14;
        result = new FSPathResult(d);
        assertEquals(d.toString(), result.toString());

        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());

        URI uri = URI.create("http://example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());

        Boolean bool = true;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());
    }
}
