

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\r\nTest"));
    }

    @Test
    public void testReadEmptyReader() throws IOException {
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(-1, emptyReader.read());
    }

    @Test
    public void testReadSingleCharacter() throws IOException {
        assertEquals('H', reader.read());
    }

    @Test
    public void testIOException() {
        ExtendedBufferedReader errorReader = new ExtendedBufferedReader(new MockReaderWithIOException());
        try {
            errorReader.read();
            fail("Expected IOException");
        } catch (IOException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testReadWithLargeBuffer() throws IOException {
        char[] buffer = new char[100];
        int length = reader.read(buffer, 0, 100);
        assertEquals(10, length);
    }

    @Test
    public void testLookAheadEndOfStream() throws IOException {
        reader.read(); // Read 'H'
        reader.read(); // Read 'e'
        reader.read(); // Read 'l'
        reader.read(); // Read 'l'
        reader.read(); // Read 'o'
        reader.read(); // Read '\n'
        assertEquals(-1, reader.lookAhead());
    }

    @Test
    public void testReadLineWithDifferentLineTerminators() throws IOException {
        String line1 = reader.readLine(); // Read "Hello\n"
        String line2 = reader.readLine(); // Read "World\r\n"
        String line3 = reader.readLine(); // Read "Test"

        assertEquals("Hello", line1);
        assertEquals("World", line2);
        assertEquals("Test", line3);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        reader.readLine(); // Read "Hello\n"
        reader.readLine(); // Read "World\r\n"
        int lineNumber = reader.getLineNumber(); // Should be 2

        assertEquals(2, lineNumber);
    }

    private class MockReaderWithIOException extends Reader {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("Mock IOException");
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }
    }
}

