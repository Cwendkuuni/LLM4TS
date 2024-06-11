package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3"));

        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('2', reader.read());
        assertEquals('\n', reader.read());
        assertEquals('l', reader.read());
        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('3', reader.read());
        assertEquals('\n', reader.read());
        assertEquals(-1, reader.read());

        assertNull(reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3"));

        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();

        assertEquals('1', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals('l', reader.readAgain());
        assertEquals('i', reader.readAgain());
        assertEquals('n', reader.readAgain());
        assertEquals('e', reader.readAgain());
        assertEquals('2', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals('l', reader.readAgain());
        assertEquals('i', reader.readAgain());
        assertEquals('n', reader.readAgain());
        assertEquals('e', reader.readAgain());
        assertEquals('3', reader.readAgain());
        assertEquals('\n', reader.readAgain());
        assertEquals(-1, reader.readAgain());

        assertNull(reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));

        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
        assertEquals("line3\n", reader.readLine());
        assertEquals(null, reader.readLine());

        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));

        assertEquals('l', reader.lookAhead());
        assertEquals('i', reader.lookAhead());
        assertEquals('n', reader.lookAhead());
        assertEquals('e', reader.lookAhead());
        assertEquals('1', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('l', reader.lookAhead());
        assertEquals('i', reader.lookAhead());
        assertEquals('n', reader.lookAhead());
        assertEquals('e', reader.lookAhead());
        assertEquals('2', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals('l', reader.lookAhead());
        assertEquals('i', reader.lookAhead());
        assertEquals('n', reader.lookAhead());
        assertEquals('e', reader.lookAhead());
        assertEquals('3', reader.lookAhead());
        assertEquals('\n', reader.lookAhead());
        assertEquals(-1, reader.lookAhead());

        assertNull(reader.lookAhead());
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));

        reader.mark(1);
        reader.read();
        reader.lookAhead();
        reader.reset();
    }

    @Test
    public void testReadWithMarkAndReset() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));

        reader.read();
        reader.mark(1);
        reader.read();
        reader.reset();

        assertEquals('i', reader.read());
        assertEquals('n', reader.read());
        assertEquals('e', reader.read());
        assertEquals('1', reader.read());
        assertEquals('\n', reader.read());
    }

    private static class StringReader extends Reader {
        private final String content;
        private int index = 0;
        private int currentLineNumber = 1;

        public StringReader(String content) {
            this.content = content;
        }

        @Override
        public int read(char[] c, int off, int len) throws IOException {
            if (len == 0) {
                return 0;
            }

            int remaining = content.length() - index;
            int length = Math.min(remaining, len);

            System.arraycopy(content.substring(index, index + length).toCharArray(), off, c, off, length);
            index += length;

            int lastIndexOfLf = content.lastIndexOf('\n', index);
            if (lastIndexOfLf > -1) {
                currentLineNumber++;
                index = lastIndexOfLf + 1;
            }

            return length;
        }

        @Override
        public int read() throws IOException {
            int c = read(new char[1], 0, 1)[0];
            return c == -1 ? -1 : c;
        }

        @Override
        public boolean markSupported(boolean readAheadSet) {
            return true;
        }

        @Override
        public void mark(int readlimit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset() throws IOException {
            index = 0;
        }

        @Override
        public void close() throws IOException {
            // no-op
        }
    }
}