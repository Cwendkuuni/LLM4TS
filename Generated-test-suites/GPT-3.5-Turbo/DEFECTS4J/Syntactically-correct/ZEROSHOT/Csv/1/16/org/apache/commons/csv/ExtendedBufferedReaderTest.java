import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        int result = br.read();
        assertEquals('H', result);
        result = br.read();
        assertEquals('e', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        br.read();
        int result = br.readAgain();
        assertEquals('H', result);
    }

    @Test
    public void testReadCharArray() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        char[] buffer = new char[5];
        int result = br.read(buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer);
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        String line = br.readLine();
        assertEquals("Hello", line);
        line = br.readLine();
        assertEquals("World", line);
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        int result = br.lookAhead();
        assertEquals('H', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);
        br.readLine();
        int result = br.getLineNumber();
        assertEquals(1, result);
    }
}