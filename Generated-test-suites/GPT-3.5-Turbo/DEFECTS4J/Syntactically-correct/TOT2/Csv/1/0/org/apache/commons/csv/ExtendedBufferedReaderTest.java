import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.CharArrayReader;
import java.io.IOException;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Test
    public void testRead() throws IOException {
        char[] input = {'a', 'b', 'c'};
        reader = new ExtendedBufferedReader(new CharArrayReader(input));
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        char[] input = {'x', 'y', 'z'};
        reader = new ExtendedBufferedReader(new CharArrayReader(input));
        reader.read();
        reader.read();
        assertEquals('z', reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "test\nline\r\nthird";
        reader = new ExtendedBufferedReader(new CharArrayReader(input.toCharArray()));
        assertEquals("test", reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("line", reader.readLine());
        assertEquals(2, reader.getLineNumber());
        assertNull(reader.readLine()); // reached EOF
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "abc";
        reader = new ExtendedBufferedReader(new CharArrayReader(input.toCharArray()));
        assertEquals('a', reader.lookAhead());
        assertEquals('a', reader.read());
    }

}