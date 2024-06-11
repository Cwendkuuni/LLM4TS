import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }
            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        try {
            int result = ebr.read();
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadAgain() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }
            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        ebr.read();
        int result = ebr.readAgain();
        assertEquals(-1, result);
    }

    @Test
    public void testReadCharArray() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                cbuf[off] = 'a';
                return 1;
            }
            @Override
            public void close() throws IOException {}
        };
        char[] buf = new char[10];
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        try {
            int result = ebr.read(buf, 0, 1);
            assertEquals(1, result);
            assertEquals('a', buf[0]);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadCharArrayWithNewLine() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                cbuf[off] = 'a';
                cbuf[off+1] = '\n';
                return 2;
            }
            @Override
            public void close() throws IOException {}
        };
        char[] buf = new char[10];
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        try {
            int result = ebr.read(buf, 0, 2);
            assertEquals(2, result);
            assertEquals('a', buf[0]);
            assertEquals('\n', buf[1]);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadLine() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }
            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        try {
            String result = ebr.readLine();
            assertNull(result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testLookAhead() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }
            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        try {
            int result = ebr.lookAhead();
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetLineNumber() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }
            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader ebr = new ExtendedBufferedReader(reader);
        int result = ebr.getLineNumber();
        assertEquals(0, result);
    }

}