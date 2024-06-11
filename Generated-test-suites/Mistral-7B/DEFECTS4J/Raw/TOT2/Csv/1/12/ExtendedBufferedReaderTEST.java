import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        assertNotNull(reader);
    }

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        int result = reader.read();
        assertEquals('h', result);

        // Testing EOF
        reader.mark(1);
        reader.read();
        reader.reset();
        assertEquals(-1, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        reader.read();
        int result = reader.readAgain();
        assertEquals('h', result);

        // Testing EOF
        reader.mark(1);
        reader.read();
        reader.reset();
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld"));
        String result = reader.readLine();
        assertEquals("hello", result);

        // Testing empty line
        reader = new ExtendedBufferedReader(new StringReader("\n"));
        result = reader.readLine();
        assertNull(result);

        // Testing EOF
        reader.mark(1);
        reader.readLine();
        reader.reset();
        result = reader.readLine();
        assertNull(result);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        int result = reader.lookAhead();
        assertEquals('h', result);

        // Testing EOF
        reader.mark(1);
        reader.read();
        reader.reset();
        result = reader.lookAhead();
        assertEquals(-1, result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int result = reader.getLineNumber();
        assertEquals(1, result);

        // Testing empty input
        reader = new ExtendedBufferedReader(new StringReader(""));
        result = reader.getLineNumber();
        assertEquals(-1, result);

        // Testing EOF
        reader.mark(1);
        reader.readLine();
        reader.reset();
        result = reader.getLineNumber();
        assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read();
        reader.read(); // Throwing IOException
    }
}