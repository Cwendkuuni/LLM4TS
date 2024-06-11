import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class FSPathResultTest {
    private FSPathResult fileResult;
    private FSPathResult stringResult;
    private FSPathResult doubleResult;
    private FSPathResult booleanResult;
    private FSPathResult dateResult;
    private FSPathResult uriResult;
    private FSPathResult emptyResult;

    @Before
    public void setUp() {
        File file = new File("/path/to/file");
        fileResult = new FSPathResult(file);
        stringResult = new FSPathResult("string");
        doubleResult = new FSPathResult(1.0);
        booleanResult = new FSPathResult(true);
        dateResult = new FSPathResult(new Date());
        uriResult = new FSPathResult(URI.create("http://example.com"));
        emptyResult = new FSPathResult(null);
    }

    @Test
    public void testGetFile() {
        assertEquals("/path/to/file", fileResult.getFile().getAbsolutePath());
        assertNull(stringResult.getFile());
        assertNull(doubleResult.getFile());
        assertNull(booleanResult.getFile());
        assertNull(dateResult.getFile());
        assertNull(uriResult.getFile());
        assertNull(emptyResult.getFile());
    }

    @Test
    public void testGetString() {
        assertNull(fileResult.getString());
        assertEquals("string", stringResult.getString());
        assertNull(doubleResult.getString());
        assertNull(booleanResult.getString());
        assertNull(dateResult.getString());
        assertNull(uriResult.getString());
        assertNull(emptyResult.getString());
    }

    @Test
    public void testGetDouble() {
        assertNull(fileResult.getDouble());
        assertNull(stringResult.getDouble());
        assertEquals(Double.valueOf(1.0), doubleResult.getDouble());
        assertNull(booleanResult.getDouble());
        assertNull(dateResult.getDouble());
        assertNull(uriResult.getDouble());
        assertNull(emptyResult.getDouble());
    }

    @Test
    public void testGetBoolean() {
        assertNull(fileResult.getBoolean());
        assertNull(stringResult.getBoolean());
        assertNull(doubleResult.getBoolean());
        assertEquals(Boolean.TRUE, booleanResult.getBoolean());
        assertNull(dateResult.getBoolean());
        assertNull(uriResult.getBoolean());
        assertNull(emptyResult.getBoolean());
    }

    @Test
    public void testGetDate() {
        assertNull(fileResult.getDate());
        assertNull(stringResult.getDate());
        assertNull(doubleResult.getDate());
        assertNull(booleanResult.getDate());
        assertEquals(new Date().toString(), dateResult.getDate().toString());
        assertNull(uriResult.getDate());
        assertNull(emptyResult.getDate());
    }

    @Test
    public void testGetURI() {
        assertNull(fileResult.getURI());
        assertNull(stringResult.getURI());
        assertNull(doubleResult.getURI());
        assertNull(booleanResult.getURI());
        assertNull(dateResult.getURI());
        assertEquals(URI.create("http://example.com"), uriResult.getURI());
        assertNull(emptyResult.getURI());
    }

    @Test
    public void testToString() {
        assertEquals("/path/to/file", fileResult.toString());
        assertEquals("string", stringResult.toString());
        assertEquals("1.0", doubleResult.toString());
        assertEquals("true", booleanResult.toString());
        assertEquals(new Date().toString(), dateResult.toString());
        assertEquals("http://example.com", uriResult.toString());
        assertEquals("FSPathResult : empty", emptyResult.toString());
    }
}