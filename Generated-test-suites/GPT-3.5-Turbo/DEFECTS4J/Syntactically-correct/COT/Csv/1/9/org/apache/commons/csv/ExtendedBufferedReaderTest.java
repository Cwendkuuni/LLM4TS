
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    private Reader mockReader;

    @Before
    public void setUp() {
        mockReader = new StringReader("test");
        reader = new ExtendedBufferedReader(mockReader);
    }
    
    @Test
    public void testRead() throws IOException {
        int result = reader.read();
        assertEquals('t', result);
    }

    @Test
    public void testReadAgain() {
        int result = reader.readAgain();
        assertEquals(-2, result);
    }

    @Test
    public void testReadArray() throws IOException {
        char[] buffer = new char[5];
        int result = reader.read(buffer, 0, 5);
        assertEquals(4, result);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
    }

    @Test
    public void testReadLine() throws IOException {
        String result = reader.readLine();
        assertEquals("test", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        int result = reader.lookAhead();
        assertEquals('t', result);
    }

    @Test
    public void testGetLineNumber() {
        int result = reader.getLineNumber();
        assertEquals(0, result);
    }

}
