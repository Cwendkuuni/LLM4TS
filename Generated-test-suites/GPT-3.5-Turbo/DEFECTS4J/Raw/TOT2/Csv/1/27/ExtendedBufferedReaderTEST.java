import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals(-1, reader.readAgain());
        reader.read();
        assertEquals('H', reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[3];
        reader.read(buf, 0, 3);
        assertArrayEquals(new char[]{'H', 'e', 'l'}, buf);
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello", reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
    }
}