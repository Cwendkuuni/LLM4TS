import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.Reader;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        Reader mockReader = new StringReader("");
        reader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(-1, reader.read());
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadChar() throws IOException {
        char[] buf = new char[10];
        assertEquals(0, reader.read(buf, 0, 0));
    }

    @Test
    public void testReadLine() throws IOException {
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadWithLineCounter() throws IOException {
        Reader mockReader = new StringReader("test\n");
        reader = new ExtendedBufferedReader(mockReader);

        assertEquals('t', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithLineCounter() throws IOException {
        Reader mockReader = new StringReader("test\n");
        reader = new ExtendedBufferedReader(mockReader);

        assertEquals("test", reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadCharWithLineCounter() throws IOException {
        Reader mockReader = new StringReader("t\n");
        reader = new ExtendedBufferedReader(mockReader);

        char[] buf = new char[10];
        reader.read(buf, 0, 1);

        assertEquals('t', buf[0]);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadAgainWithLineCounter() throws IOException {
        Reader mockReader = new StringReader("test\n");
        reader = new ExtendedBufferedReader(mockReader);

        reader.read();
        assertEquals('t', reader.readAgain());
    }
}