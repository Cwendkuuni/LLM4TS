import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader extendedBufferedReader;

    @Before
    public void setUp() {
        extendedBufferedReader = new ExtendedBufferedReader(new Reader() {
            public int read(char[] cbuf, int off, int len) {
                // dummy implementation
                return 0;
            }
            public void close() {}
        });
    }

    @Test
    public void testRead() throws Exception {
        int expectedLastChar = UNDEFINED;

        int actualLastChar = extendedBufferedReader.read();

        assertEquals(expectedLastChar, actualLastChar);
    }

    @Test
    public void testReadAgain() {
        int expectedLastChar = UNDEFINED;

        int actualLastChar = extendedBufferedReader.readAgain();

        assertEquals(expectedLastChar, actualLastChar);
    }

    @Test
    public void testReadCharArrayIntInt() throws Exception {
        char[] buf = new char[5];
        int offset = 0;
        int length = 5;
        int expectedLen = 0;

        int actualLen = extendedBufferedReader.read(buf, offset, length);

        assertEquals(expectedLen, actualLen);
    }

    @Test
    public void testReadLine() throws Exception {
        String expectedLine = null;

        String actualLine = extendedBufferedReader.readLine();

        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testLookAhead() throws Exception {
        int expectedLookAhead = UNDEFINED;

        int actualLookAhead = extendedBufferedReader.lookAhead();

        assertEquals(expectedLookAhead, actualLookAhead);
    }

    @Test
    public void testGetLineNumber() {
        int expectedLineNumber = 0;

        int actualLineNumber = extendedBufferedReader.getLineNumber();

        assertEquals(expectedLineNumber, actualLineNumber);
    }
}