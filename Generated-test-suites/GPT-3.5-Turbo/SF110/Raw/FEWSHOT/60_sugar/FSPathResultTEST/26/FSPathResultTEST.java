
package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathResultTest {
    @Test
    public void testFileConstructor() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testStringConstructor() {
        String str = "test";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.getString());
    }

    @Test
    public void testDoubleConstructor() {
        Double dbl = 10.0;
        FSPathResult result = new FSPathResult(dbl);
        assertEquals(dbl, result.getDouble());
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
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());

        String str = "test";
        result = new FSPathResult(str);
        assertEquals(str, result.toString());

        Double dbl = 10.0;
        result = new FSPathResult(dbl);
        assertEquals(dbl.toString(), result.toString());

        Boolean bool = false;
        result = new FSPathResult(bool);
        assertEquals(bool.toString(), result.toString());

        Date date = new Date();
        result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());

        URI uri = URI.create("http://www.example.com");
        result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());

        result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }
}
