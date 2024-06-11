
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {
    private static final File TEST_FILE = new File("testFile.txt");
    private static final String TEST_STRING = "testString";
    private static final Double TEST_DOUBLE = 3.14;
    private static final Date TEST_DATE = new Date();
    private static final URI TEST_URI = URI.create("http://example.com");
    private static final Boolean TEST_BOOLEAN = true;

    @Test
    public void testConstructorWithFile() {
        FSPathResult result = new FSPathResult(TEST_FILE);
        assertNotNull(result);
        assertEquals(TEST_FILE, result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithString() {
        FSPathResult result = new FSPathResult(TEST_STRING);
        assertNotNull(result);
        assertNull(result.getFile());
        assertEquals(TEST_STRING, result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithDouble() {
        FSPathResult result = new FSPathResult(TEST_DOUBLE);
        assertNotNull(result);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(TEST_DOUBLE, result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithDate() {
        FSPathResult result = new FSPathResult(TEST_DATE);
        assertNotNull(result);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertEquals(TEST_DATE, result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithURI() {
        FSPathResult result = new FSPathResult(TEST_URI);
        assertNotNull(result);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertEquals(TEST_URI, result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testConstructorWithBoolean() {
        FSPathResult result = new FSPathResult(TEST_BOOLEAN);
        assertNotNull(result);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(TEST_BOOLEAN, result.getBoolean());
    }

    @Test
    public void testToString() {
        FSPathResult fileResult = new FSPathResult(TEST_FILE);
        assertEquals(TEST_FILE.getAbsolutePath(), fileResult.toString());

        FSPathResult stringResult = new FSPathResult(TEST_STRING);
        assertEquals(TEST_STRING, stringResult.toString());

        FSPathResult doubleResult = new FSPathResult(TEST_DOUBLE);
        assertEquals(TEST_DOUBLE.toString(), doubleResult.toString());

        FSPathResult booleanResult = new FSPathResult(TEST_BOOLEAN);
        assertEquals(TEST_BOOLEAN.toString(), booleanResult.toString());

        FSPathResult dateResult = new FSPathResult(TEST_DATE);
        assertEquals(TEST_DATE.toString(), dateResult.toString());

        FSPathResult uriResult = new FSPathResult(TEST_URI);
        assertEquals(TEST_URI.toString(), uriResult.toString());

        FSPathResult emptyResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", emptyResult.toString());
    }
}
