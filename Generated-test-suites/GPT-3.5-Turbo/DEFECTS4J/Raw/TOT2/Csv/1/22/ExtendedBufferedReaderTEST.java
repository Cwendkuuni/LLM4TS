import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("abcdef\nghijk");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.read());
        assertEquals('\n', ebr.read());
        assertEquals('g', ebr.read());
        assertEquals(1, ebr.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("abcdef\nghijk");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        ebr.read();
        assertEquals('a', ebr.readAgain());
    }

    @Test
    public void testReadArray() throws IOException {
        StringReader reader = new StringReader("ab\nc\rde\nf\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        char[] buf = new char[4];

        ebr.read(buf, 0, 4);

        assertEquals('a', buf[0]);
        assertEquals('b', buf[1]);
        assertEquals('\n', buf[2]);
        assertEquals('c', buf[3]);
        assertEquals(2, ebr.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("abc\n");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals("abc", ebr.readLine());
        assertEquals(1, ebr.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("abcd");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        assertEquals('a', ebr.lookAhead());
        assertEquals('a', ebr.read());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("abc\ndef\nghi");
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);

        int lineNumber = ebr.getLineNumber();
        ebr.readLine();
        assertEquals(lineNumber + 1, ebr.getLineNumber());
        ebr.readLine();
        assertEquals(lineNumber + 2, ebr.getLineNumber());
    }
}