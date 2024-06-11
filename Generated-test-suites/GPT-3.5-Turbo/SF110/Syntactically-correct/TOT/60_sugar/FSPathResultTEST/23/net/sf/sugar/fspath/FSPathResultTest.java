import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    private FSPathResult fileResult;
    private FSPathResult stringResult;
    private FSPathResult doubleResult;
    private FSPathResult dateResult;
    private FSPathResult uriResult;
    private FSPathResult booleanResult;

    @Before
    public void setUp() {
        fileResult = new FSPathResult(new File("testFile.txt"));
        stringResult = new FSPathResult("test");
        doubleResult = new FSPathResult(10.5);
        dateResult = new FSPathResult(new Date());
        uriResult = new FSPathResult(URI.create("http://www.example.com"));
        booleanResult = new FSPathResult(true);
    }

    @Test
    public void testGetFile() {
        File file = fileResult.getFile();
        assertNotNull(file);
        assertEquals("testFile.txt", file.getName());
    }

    @Test
    public void testGetString() {
        String str = stringResult.getString();
        assertNotNull(str);
        assertEquals("test", str);
    }

    @Test
    public void testGetDouble() {
        double number = doubleResult.getDouble();
        assertEquals(10.5, number, 0.001);
    }

    @Test
    public void testGetDate() {
        Date date = dateResult.getDate();
        assertNotNull(date);
    }

    @Test
    public void testGetURI() {
        URI uri = uriResult.getURI();
        assertNotNull(uri);
        assertEquals("http://www.example.com", uri.toString());
    }

    @Test
    public void testGetBoolean() {
        Boolean bool = booleanResult.getBoolean();
        assertNotNull(bool);
        assertTrue(bool);
    }

    @Test
    public void testToStringWithFile() {
        String str = fileResult.toString();
        assertNotNull(str);
        assertEquals("testFile.txt", str);
    }

    @Test
    public void testToStringWithString() {
        String str = stringResult.toString();
        assertNotNull(str);
        assertEquals("test", str);
    }

    @Test
    public void testToStringWithDouble() {
        String str = doubleResult.toString();
        assertNotNull(str);
        assertEquals("10.5", str);
    }

    @Test
    public void testToStringWithBoolean() {
        String str = booleanResult.toString();
        assertNotNull(str);
        assertEquals("true", str);
    }

    @Test
    public void testToStringWithDate() {
        String str = dateResult.toString();
        assertNotNull(str);
    }

    @Test
    public void testToStringWithURI() {
        String str = uriResult.toString();
        assertNotNull(str);
        assertEquals("http://www.example.com", str);
    }
}