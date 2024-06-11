import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        assertEquals('h', extendedReader.read());
        assertEquals('e', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('o', extendedReader.read());
        assertEquals('\n', extendedReader.read());
        assertEquals('w', extendedReader.read());
        assertEquals('o', extendedReader.read());
        assertEquals('r', extendedReader.read());
        assertEquals('l', extendedReader.read());
        assertEquals('d', extendedReader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("hello");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        extendedReader.read();
        assertEquals('h', extendedReader.readAgain());

        extendedReader.read();
        assertEquals('e', extendedReader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        char[] buffer = new char[5];
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int bytesRead = extendedReader.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("hello", new String(buffer));

        bytesRead = extendedReader.read(buffer, 0, 5);
        assertEquals(5, bytesRead);
        assertEquals("\nworl", new String(buffer));
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("hello\nworld");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        assertEquals("hello", extendedReader.readLine());
        assertEquals("world", extendedReader.readLine());
        assertNull(extendedReader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("hello");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        assertEquals('h', extendedReader.lookAhead());
        assertEquals('h', extendedReader.read());
        assertEquals('e', extendedReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("hello\nworld\n");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        assertEquals(0, extendedReader.getLineNumber());
        extendedReader.read();
        assertEquals(1, extendedReader.getLineNumber());
        extendedReader.readLine();
        assertEquals(2, extendedReader.getLineNumber());
    }
}