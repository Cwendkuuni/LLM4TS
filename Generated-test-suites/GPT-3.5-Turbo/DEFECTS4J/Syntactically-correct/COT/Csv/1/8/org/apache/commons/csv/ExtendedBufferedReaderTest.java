import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("Test String");
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws Exception {
        int result = reader.read();
        assertEquals('T', result);
    }

    @Test
    public void testReadAgain() throws Exception {
        int result = reader.readAgain();
        assertEquals(-2, result);
    }

    @Test
    public void testReadWithBuffer() throws Exception {
        char[] buffer = new char[5];
        int result = reader.read(buffer, 0, 5);
        assertEquals(5, result);
        assertEquals("Test ", new String(buffer));
    }

    @Test
    public void testReadLine() throws Exception {
        String result = reader.readLine();
        assertEquals("Test String", result);
    }

    @Test
    public void testLookAhead() throws Exception {
        int result = reader.lookAhead();
        assertEquals('e', result);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        int result = reader.getLineNumber();
        assertEquals(0, result);
    }

    @Test(expected = IOException.class)
    public void testReadWithEmptyBuffer() throws Exception {
        char[] buffer = new char[5];
        int result = reader.read(buffer, 0, 5);
        assertEquals(0, result);
        assertEquals("", new String(buffer));
    }

    @Test(expected = IOException.class)
    public void testReadLineWithEmptyBuffer() throws Exception {
        String result = reader.readLine();
        assertEquals("", result);
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithEmptyBuffer() throws Exception {
        int result = reader.lookAhead();
        assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testReadWithShortBuffer() throws Exception {
        char[] buffer = new char[10];
        int result = reader.read(buffer, 0, 10);
        assertEquals(5, result);
        assertEquals("Test Strin", new String(buffer));
    }
}