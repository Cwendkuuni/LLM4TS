import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader sr = new StringReader("Line 1\nLine 2\r\nLine 3\n");
        reader = new ExtendedBufferedReader(sr);
    }

    @Test
    public void testRead() throws IOException {
        int c1 = reader.read(); // Read 'L'
        int c2 = reader.read(); // Read 'i'
        int c3 = reader.read(); // Read 'n'
        int c4 = reader.read(); // Read 'e'
        int c5 = reader.read(); // Read ' '
        
        assertEquals('L', c1);
        assertEquals('i', c2);
        assertEquals('n', c3);
        assertEquals('e', c4);
        assertEquals(' ', c5);
    }

    @Test
    public void testReadAgain() throws IOException {
        int c1 = reader.read(); // Read 'L'
        int c2 = reader.read(); // Read 'i'
        int c3 = reader.read(); // Read 'n'
        int c4 = reader.read(); // Read 'e'

        int lastChar = reader.readAgain();
        
        assertEquals('e', lastChar);
    }

    @Test
    public void testReadBuffer() throws IOException {
        char[] buffer = new char[10];
        int len = reader.read(buffer, 0, 10);

        assertEquals(10, len);
        assertEquals('L', buffer[0]);
        assertEquals('i', buffer[1]);
        assertEquals('n', buffer[2]);
        assertEquals('e', buffer[3]);
        assertEquals(' ', buffer[4]);
        assertEquals('1', buffer[5]);
        assertEquals('L', buffer[6]);
        assertEquals('i', buffer[7]);
        assertEquals('n', buffer[8]);
        assertEquals('e', buffer[9]);
    }

    @Test
    public void testReadLine() throws IOException {
        String line1 = reader.readLine(); // Read 'Line 1'
        String line2 = reader.readLine(); // Read 'Line 2'
        String line3 = reader.readLine(); // Read 'Line 3'
        String line4 = reader.readLine(); // Read null
        
        assertEquals("Line 1", line1);
        assertEquals("Line 2", line2);
        assertEquals("Line 3", line3);
        assertNull(line4);
    }

    @Test
    public void testLookAhead() throws IOException {
        int c1 = reader.lookAhead(); // Peek 'L'

        assertEquals('L', c1);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int line1 = reader.getLineNumber(); // Read 'Line 1'
        int line2 = reader.getLineNumber(); // Read 'Line 2'
        int line3 = reader.getLineNumber(); // Read 'Line 3'
        
        assertEquals(1, line1);
        assertEquals(2, line2);
        assertEquals(3, line3);
    }
}