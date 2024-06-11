import org.junit.*;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private FSPathResult fsPathResult;

    @Before
    public void setUp() {
        File file = new File("testFile.txt");
        fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testGetFile() {
        File expectedFile = new File("testFile.txt");
        assertEquals(expectedFile, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        assertNull(fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        assertNull(fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        assertNull(fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        assertNull(fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        String expectedString = "testFile.txt";
        assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testToStringWithString() {
        fsPathResult = new FSPathResult("testString");
        String expectedString = "testString";
        assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testToStringWithDouble() {
        fsPathResult = new FSPathResult(10.5);
        String expectedString = "10.5";
        assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        fsPathResult = new FSPathResult(true);
        String expectedString = "true";
        assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testToStringWithDate() {
        fsPathResult = new FSPathResult(new Date());
        assertNotNull(fsPathResult.toString());
    }

    @Test
    public void testToStringWithURI() {
        fsPathResult = new FSPathResult(URI.create("http://example.com"));
        String expectedString = "http://example.com";
        assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testToStringWithEmptyResult() {
        fsPathResult = new FSPathResult(null);
        String expectedString = "FSPathResult : empty";
        assertEquals(expectedString, fsPathResult.toString());
    }
}