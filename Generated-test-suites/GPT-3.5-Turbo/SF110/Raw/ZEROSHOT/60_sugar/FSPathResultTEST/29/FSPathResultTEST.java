import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    private FSPathResult fsPathResult;

    @Before
    public void setUp() {
        File file = new File("testfile.txt");
        fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testGetFile() {
        File result = fsPathResult.getFile();
        assertNotNull(result);
        assertEquals("testfile.txt", result.getName());
        assertEquals("testfile.txt", result.getAbsolutePath());
    }

    @Test
    public void testGetString() {
        fsPathResult = new FSPathResult("test");
        String result = fsPathResult.getString();
        assertNotNull(result);
        assertEquals("test", result);
    }

    @Test
    public void testGetDouble() {
        fsPathResult = new FSPathResult(1.5);
        Double result = fsPathResult.getDouble();
        assertNotNull(result);
        assertEquals(1.5, result, 0.001);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        fsPathResult = new FSPathResult(date);
        Date result = fsPathResult.getDate();
        assertNotNull(result);
        assertEquals(date, result);
    }

    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        fsPathResult = new FSPathResult(uri);
        URI result = fsPathResult.getURI();
        assertNotNull(result);
        assertEquals(uri, result);
    }

    @Test
    public void testGetBoolean() {
        fsPathResult = new FSPathResult(true);
        Boolean result = fsPathResult.getBoolean();
        assertNotNull(result);
        assertTrue(result);
    }

    @Test
    public void testToString() {
        String result = fsPathResult.toString();
        assertNotNull(result);
        assertEquals("testfile.txt", result);
    }
}