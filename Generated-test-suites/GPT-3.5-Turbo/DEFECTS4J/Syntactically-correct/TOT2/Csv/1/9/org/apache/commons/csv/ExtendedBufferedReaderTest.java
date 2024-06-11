import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader extendedBufferedReader;

    @Before
    public void setup() {
        extendedBufferedReader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', extendedBufferedReader.read());
    }

    @Test
    public void testReadAgain() {
        assertEquals('H', extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadArray() throws IOException {
        char[] buf = new char[5];
        assertEquals(5, extendedBufferedReader.read(buf, 0, 5));
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello", extendedBufferedReader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', extendedBufferedReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(1, extendedBufferedReader.getLineNumber());
    }

}