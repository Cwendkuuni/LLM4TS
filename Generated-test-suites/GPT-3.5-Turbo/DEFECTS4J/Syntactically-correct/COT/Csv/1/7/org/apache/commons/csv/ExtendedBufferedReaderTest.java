
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello, World!"));
        int result = reader.read();
        assertEquals('H', result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello, World!"));
        char[] buffer = new char[5];
        int result = reader.read(buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer);
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        String result = reader.readLine();
        assertEquals("Hello", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello, World!"));
        int result = reader.lookAhead();
        assertEquals('H', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld!"));
        reader.readLine();
        int result = reader.getLineNumber();
        assertEquals(1, result);
    }

    @Test
    public void testReadEmptyFile() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int result = reader.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello, World!"));
        char[] buffer = new char[10];
        int result = reader.read(buffer, 5, 10);
        assertEquals(5, result);
        assertArrayEquals(new char[]{0, 0, 0, 0, 0, 'H', 'e', 'l', 'l', 'o'}, buffer);
    }

    @Test
    public void testReadLineWithSpecialCharacters() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("!@#$%^&*()"));
        String result = reader.readLine();
        assertEquals("!@#$%^&*()", result);
    }

    @Test
    public void testReadWithIOException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public void close() throws IOException {
            }
        });
        try {
            reader.read();
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }

    @Test
    public void testReadLineWithLineTerminator() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\n"));
        String result = reader.readLine();
        assertEquals("Hello", result);
    }

}
