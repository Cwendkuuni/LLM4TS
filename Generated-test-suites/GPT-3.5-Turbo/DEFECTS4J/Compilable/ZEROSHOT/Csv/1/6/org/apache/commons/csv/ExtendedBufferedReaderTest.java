package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private static class TestReader extends Reader {
        private String input;
        private int position;

        public TestReader(String input) {
            this.input = input;
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            if (position == input.length()) {
                return -1;
            }
            int lenToRead = Math.min(len, input.length() - position);
            for (int i = 0; i < lenToRead; i++) {
                cbuf[i] = input.charAt(position++);
            }
            return lenToRead;
        }

        public void close() throws IOException {
        }
    }

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new TestReader("Hello World"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        assertEquals('H', reader.read());
        assertEquals('e', reader.read());
        assertEquals('l', reader.read());
        assertEquals('l', reader.read());
        assertEquals('o', reader.read());
        assertEquals(' ', reader.read());
        assertEquals(1, reader.readAgain());
        assertEquals('W', reader.read());
        assertEquals('o', reader.read());
        assertEquals('r', reader.read());
        assertEquals('l', reader.read());
        assertEquals('d', reader.read());
        assertEquals(11, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello World", reader.readLine());
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('H', reader.read());
        assertEquals('e', reader.lookAhead());
        assertEquals('e', reader.lookAhead());
        assertEquals('e', reader.read());
        assertEquals('l', reader.lookAhead());
        assertEquals('l', reader.read());
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        assertEquals(5, reader.read(buf, 0, 5));
        assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buf);
        assertEquals(3, reader.getLineNumber());

        buf = new char[11];
        assertEquals(6, reader.read(buf, 0, 11));
        assertArrayEquals(new char[]{' ', 'W', 'o', 'r', 'l', 'd', 0, 0, 0, 0, 0}, buf);
        assertEquals(7, reader.getLineNumber());
    }
}