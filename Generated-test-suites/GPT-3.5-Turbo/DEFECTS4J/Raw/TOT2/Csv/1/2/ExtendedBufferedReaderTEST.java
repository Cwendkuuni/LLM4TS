import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Sample\nInput\r\nData"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals((int)'S', reader.read());
        assertEquals((int)'a', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals((int)'a', reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[4];
        reader.read(buf, 0, 4);
        assertEquals("Samp", new String(buf));
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Sample", reader.readLine());
        assertEquals("Input", reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals((int)'a', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
    }

}