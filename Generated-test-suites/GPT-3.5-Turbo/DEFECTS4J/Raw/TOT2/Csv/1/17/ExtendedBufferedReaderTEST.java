import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Test String\nSecond Line\rThird Line\r\n"));
    }

    @After
    public void tearDown() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('T', reader.read());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals('T', reader.readAgain());
    }

    @Test
    public void testReadBuffer() throws IOException {
        char[] buffer = new char[7];
        reader.read(buffer, 0, 7);
        assertEquals("est Str", new String(buffer));
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Test String", reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', reader.lookAhead());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, reader.getLineNumber());
    }

}