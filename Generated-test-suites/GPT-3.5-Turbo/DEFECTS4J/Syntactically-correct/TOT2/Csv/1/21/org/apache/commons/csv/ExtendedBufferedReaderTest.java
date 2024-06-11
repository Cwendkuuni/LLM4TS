import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        reader = new ExtendedBufferedReader(new StringReader("Test line 1\nTest line 2\r\nTest line 3"));
    }

    @Test
    public void testRead() {
        assertEquals('T', reader.read());
        assertEquals(0, reader.getLineNumber());
        assertEquals('e', reader.read());
        assertEquals(1, reader.getLineNumber());
        assertEquals(1, reader.readLine().length());
        assertEquals('s', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() {
        reader.read();
        assertEquals('T', reader.readAgain());
    }

    @Test
    public void testReadToEnd() {
        char[] buf = new char[10];
        int len = reader.read(buf, 0, 10);
        assertEquals(10, len);
        assertEquals('e', buf[9]);
        assertTrue(reader.getLineNumber() > 0);
    }

    @Test
    public void testReadLine() {
        String line1 = reader.readLine();
        assertEquals("Test line 1", line1);
        String line2 = reader.readLine();
        assertEquals("Test line 2", line2);
        String line3 = reader.readLine();
        assertEquals("Test line 3", line3);
        assertNull(reader.readLine());
        assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() {
        try {
            assertEquals('T', reader.lookAhead());
            assertEquals('T', reader.read());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testEndOfStream() {
        try {
            reader.read();
            reader.readLine();
            reader.read(new char[10], 0, 10);
            assertEquals(-1, reader.lookAhead());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}