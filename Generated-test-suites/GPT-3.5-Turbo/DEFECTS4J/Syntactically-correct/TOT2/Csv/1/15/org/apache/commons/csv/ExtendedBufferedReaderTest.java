import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Test\nline\r\nPlease"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('T', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals('T', reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[4];
        assertEquals(4, reader.read(buf, 0, 4));
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Test", reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
    }

    // Add additional tests for edge cases and error scenarios

}