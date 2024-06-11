import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\r\nStackOverflow"));
    }

    @Test
    public void testRead() throws IOException {
        int c = reader.read();
        assertEquals('H', c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('e', c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('l', c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('l', c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('o', c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('W', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('o', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('r', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('l', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('d', c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('\r', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('S', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('t', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('a', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('c', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('k', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('O', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('v', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('e', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('r', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('f', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('l', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('o', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('w', c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals(-1, c);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        int c = reader.read();
        assertEquals('H', c);

        c = reader.read();
        assertEquals('e', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('\n', c);

        c = reader.read();
        assertEquals('W', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('r', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('d', c);

        c = reader.read();
        assertEquals('\r', c);

        c = reader.read();
        assertEquals('\n', c);

        c = reader.read();
        assertEquals('S', c);

        c = reader.read();
        assertEquals('t', c);

        c = reader.read();
        assertEquals('a', c);

        c = reader.read();
        assertEquals('c', c);

        c = reader.read();
        assertEquals('k', c);

        c = reader.read();
        assertEquals('O', c);

        c = reader.read();
        assertEquals('v', c);

        c = reader.read();
        assertEquals('e', c);

        c = reader.read();
        assertEquals('r', c);

        c = reader.read();
        assertEquals('f', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('w', c);

        c = reader.read();
        assertEquals(-1, c);

        c = reader.readAgain();
        assertEquals(-1, c);
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        int len = reader.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals("Hello", new String(buf));
        assertEquals(1, reader.getLineNumber());

        len = reader.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals("nWorl", new String(buf));
        assertEquals(2, reader.getLineNumber());

        len = reader.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals("d\r\nSt", new String(buf));
        assertEquals(3, reader.getLineNumber());

        len = reader.read(buf, 0, 5);
        assertEquals(4, len);
        assertEquals("ackO", new String(buf, 0, len));
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals("Hello", line);
        assertEquals(1, reader.getLineNumber());

        line = reader.readLine();
        assertEquals("World", line);
        assertEquals(2, reader.getLineNumber());

        line = reader.readLine();
        assertEquals("StackOverflow", line);
        assertEquals(3, reader.getLineNumber());

        line = reader.readLine();
        assertNull(line);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        int c = reader.lookAhead();
        assertEquals('H', c);

        c = reader.read();
        assertEquals('H', c);

        c = reader.lookAhead();
        assertEquals('e', c);

        c = reader.read();
        assertEquals('e', c);

        c = reader.lookAhead();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.lookAhead();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.lookAhead();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.lookAhead();
        assertEquals('\n', c);

        c = reader.read();
        assertEquals('\n', c);

        c = reader.lookAhead();
        assertEquals('W', c);

        c = reader.read();
        assertEquals('W', c);

        c = reader.lookAhead();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.lookAhead();
        assertEquals('r', c);

        c = reader.read();
        assertEquals('r', c);

        c = reader.lookAhead();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.lookAhead();
        assertEquals('d', c);

        c = reader.read();
        assertEquals('d', c);

        c = reader.lookAhead();
        assertEquals('\r', c);

        c = reader.read();
        assertEquals('\r', c);

        c = reader.lookAhead();
        assertEquals('\n', c);

        c = reader.read();
        assertEquals('\n', c);

        c = reader.lookAhead();
        assertEquals('S', c);

        c = reader.read();
        assertEquals('S', c);

        c = reader.lookAhead();
        assertEquals('t', c);

        c = reader.read();
        assertEquals('t', c);

        c = reader.lookAhead();
        assertEquals('a', c);

        c = reader.read();
        assertEquals('a', c);

        c = reader.lookAhead();
        assertEquals('c', c);

        c = reader.read();
        assertEquals('c', c);

        c = reader.lookAhead();
        assertEquals('k', c);

        c = reader.read();
        assertEquals('k', c);

        c = reader.lookAhead();
        assertEquals('O', c);

        c = reader.read();
        assertEquals('O', c);

        c = reader.lookAhead();
        assertEquals('v', c);

        c = reader.read();
        assertEquals('v', c);

        c = reader.lookAhead();
        assertEquals('e', c);

        c = reader.read();
        assertEquals('e', c);

        c = reader.lookAhead();
        assertEquals('r', c);

        c = reader.read();
        assertEquals('r', c);

        c = reader.lookAhead();
        assertEquals('f', c);

        c = reader.read();
        assertEquals('f', c);

        c = reader.lookAhead();
        assertEquals('l', c);

        c = reader.read();
        assertEquals('l', c);

        c = reader.lookAhead();
        assertEquals('o', c);

        c = reader.read();
        assertEquals('o', c);

        c = reader.lookAhead();
        assertEquals('w', c);

        c = reader.read();
        assertEquals('w', c);

        c = reader.lookAhead();
        assertEquals(-1, c);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());

        reader.read();
        reader.read();
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals(2, reader.getLineNumber());

        reader.read();
        reader.read();
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals(2, reader.getLineNumber());

        reader.read();
        reader.read();
        assertEquals(3, reader.getLineNumber());

        reader.readLine();
        assertEquals(4, reader.getLineNumber());

        reader.readLine();
        assertEquals(5, reader.getLineNumber());
    }
}