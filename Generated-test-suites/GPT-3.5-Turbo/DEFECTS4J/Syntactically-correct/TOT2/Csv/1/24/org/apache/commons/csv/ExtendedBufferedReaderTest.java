import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() throws IOException {
        String input = "Test input string\nwith multiple lines";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int expected = 'T';
        int actual = reader.read();
        assertEquals(expected, actual);
        assertEquals(1, reader.getLineNumber());
    }
    
    @Test
    public void testReadAgain() throws IOException {
        String input = "Testing readAgain method";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int expected = 'T';
        reader.read(); // Read 'T'
        int actual = reader.readAgain();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReadArray() throws IOException {
        String input = "Line 1\r\nLine 2\nLine 3\rLine 4";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        char[] buf = new char[10];
        reader.read(buf, 0, 10);
        assertEquals(10, reader.getLineNumber());
    }
    
    @Test
    public void testReadLine() throws IOException {
        String input = "Line 1\nLine 2\rLine 3\n\n";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        assertEquals("Line 1", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("Line 2", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertNull(reader.readLine());
        assertEquals(-1, reader.getLineNumber());
    }
    
    @Test
    public void testLookAhead() throws IOException {
        String input = "Test lookAhead method";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));
        int expected = 'T';
        int actual = reader.lookAhead();
        assertEquals(expected, actual);
    }
    
}