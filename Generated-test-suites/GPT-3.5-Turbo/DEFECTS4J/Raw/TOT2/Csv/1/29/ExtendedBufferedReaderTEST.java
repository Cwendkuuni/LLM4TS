import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        // Initialize ExtendedBufferedReader with a StringReader object
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\r\nTest"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
        // Testing for line count increment on encountering '\n'
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() {
        reader.read();
        assertEquals('H', reader.readAgain());
    }

    @Test
    public void testReadArray() throws IOException {
        char[] buf = new char[5];
        reader.read(buf, 0, buf.length);
        assertEquals('l', buf[3]);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello", reader.readLine());
        // Testing for line count increment on reading a line
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadEOF() throws IOException {
        // Read characters until end of file
        reader.read();
        reader.read();
        reader.read();
        assertEquals(-1, reader.read()); // EOF should return END_OF_STREAM
    }

    @Test
    public void testReadArrayEOF() throws IOException {
        char[] buf = new char[10];
        int len = reader.read(buf, 0, buf.length);
        assertEquals(-1, len); // EOF should return -1
    }

    @Test
    public void testReadLineEOF() throws IOException {
        // Read all lines till end of file
        reader.readLine();
        reader.readLine();
        assertEquals(null, reader.readLine()); // EOF should return null
    }
}