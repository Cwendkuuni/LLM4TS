import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class ExtendedBufferedReaderTest {

    private StringReader stringReader;

    @Test
    public void testRead() throws IOException {
        stringReader = new StringReader("abc");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        int result = reader.read();
        assertEquals('a', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        stringReader = new StringReader("abc");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        reader.read();
        int result = reader.readAgain();
        assertEquals('a', result);
    }

    @Test
    public void testReadLine() throws IOException {
        stringReader = new StringReader("abc\n123\nxyz");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        String result = reader.readLine();
        assertEquals("abc", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        stringReader = new StringReader("abc");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        int result = reader.lookAhead();
        assertEquals('a', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        stringReader = new StringReader("abc\n123\nxyz");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(2, result);
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        stringReader = new StringReader("");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        int result = reader.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadMultipleChars() throws IOException {
        stringReader = new StringReader("abc");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);
        char[] buf = new char[3];
        int result = reader.read(buf, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(new char[]{'a', 'b', 'c'}, buf);
    }
}

