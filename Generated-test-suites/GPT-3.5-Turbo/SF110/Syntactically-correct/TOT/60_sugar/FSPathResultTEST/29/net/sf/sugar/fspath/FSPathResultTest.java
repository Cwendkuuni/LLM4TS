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
        File file = new File("example.txt");
        fileResult = new FSPathResult(file);
        
        stringResult = new FSPathResult("example");
        
        doubleResult = new FSPathResult(3.14);
        
        dateResult = new FSPathResult(new Date());
        
        URI uri = URI.create("http://www.example.com");
        uriResult = new FSPathResult(uri);
        
        booleanResult = new FSPathResult(true);
    }

    @Test
    public void testGetFile() {
        assertEquals("example.txt", fileResult.getFile().getName());
        assertNull(stringResult.getFile());
        assertNull(doubleResult.getFile());
        assertNull(dateResult.getFile());
        assertNull(uriResult.getFile());
        assertNull(booleanResult.getFile());
    }

    @Test
    public void testGetString() {
        assertNull(fileResult.getString());
        assertEquals("example", stringResult.getString());
        assertNull(doubleResult.getString());
        assertNull(dateResult.getString());
        assertNull(uriResult.getString());
        assertNull(booleanResult.getString());
    }

    @Test
    public void testGetDouble() {
        assertNull(fileResult.getDouble());
        assertNull(stringResult.getDouble());
        assertEquals(3.14, doubleResult.getDouble(), 0);
        assertNull(dateResult.getDouble());
        assertNull(uriResult.getDouble());
        assertNull(booleanResult.getDouble());
    }

    @Test
    public void testGetDate() {
        assertNull(fileResult.getDate());
        assertNull(stringResult.getDate());
        assertNull(doubleResult.getDate());
        assertNotNull(dateResult.getDate());
        assertNull(uriResult.getDate());
        assertNull(booleanResult.getDate());
    }

    @Test
    public void testGetURI() {
        assertNull(fileResult.getURI());
        assertNull(stringResult.getURI());
        assertNull(doubleResult.getURI());
        assertNull(dateResult.getURI());
        assertEquals(URI.create("http://www.example.com"), uriResult.getURI());
        assertNull(booleanResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        assertNull(fileResult.getBoolean());
        assertNull(stringResult.getBoolean());
        assertNull(doubleResult.getBoolean());
        assertNull(dateResult.getBoolean());
        assertNull(uriResult.getBoolean());
        assertTrue(booleanResult.getBoolean());
    }

    @Test
    public void testToString() {
        assertEquals("example.txt", fileResult.toString());
        assertEquals("example", stringResult.toString());
        assertEquals("3.14", doubleResult.toString());
        assertNotNull(dateResult.toString());
        assertEquals("http://www.example.com", uriResult.toString());
        assertEquals("true", booleanResult.toString());
    }
}