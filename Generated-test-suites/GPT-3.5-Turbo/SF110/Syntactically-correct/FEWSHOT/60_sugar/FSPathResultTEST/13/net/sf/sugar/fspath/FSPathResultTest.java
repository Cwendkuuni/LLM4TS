import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {
    @Test
    public void testGetFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult result = new FSPathResult("test");
        assertEquals("test", result.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult result = new FSPathResult(3.14);
        assertEquals(Double.valueOf(3.14), result.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date(2022, 10, 30);
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
        FSPathResult result = new FSPathResult(true);
        assertEquals(Boolean.TRUE, result.getBoolean());
    }

    @Test
    public void testToString() {
        File file = new File("test.txt");
        FSPathResult fileResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fileResult.toString());

        FSPathResult stringResult = new FSPathResult("test");
        assertEquals("test", stringResult.toString());

        FSPathResult doubleResult = new FSPathResult(3.14);
        assertEquals("3.14", doubleResult.toString());

        FSPathResult booleanResult = new FSPathResult(true);
        assertEquals("true", booleanResult.toString());

        Date date = new Date(2022, 10, 30);
        FSPathResult dateResult = new FSPathResult(date);
        assertEquals(date.toString(), dateResult.toString());

        URI uri = URI.create("http://example.com");
        FSPathResult uriResult = new FSPathResult(uri);
        assertEquals(uri.toString(), uriResult.toString());

        FSPathResult emptyResult = new FSPathResult((File)null);
        assertEquals("FSPathResult : empty", emptyResult.toString());
    }
}