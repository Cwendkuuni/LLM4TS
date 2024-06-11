import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        Reader r = new StringReader("This is a test string\nTest string with\r\nmultiple lines\n");
        reader = new ExtendedBufferedReader(r);
    }

    @Test
    public void testRead() throws IOException {
        int charOne = reader.read(); // Read 'T'
        int charTwo = reader.read(); // Read 'h'
        int charThree = reader.read(); // Read 'i'

        assertEquals('T', charOne);
        assertEquals('h', charTwo);
        assertEquals('i', charThree);
    }

    @Test
    public void testReadAgain() throws IOException {
        int charOne = reader.read(); // Read 'T'
        int charTwo = reader.read(); // Read 'h'
        int charThree = reader.read(); // Read 'i'
        int lastChar = reader.readAgain();

        assertEquals('T', charOne);
        assertEquals('h', charTwo);
        assertEquals('i', charThree);
        assertEquals('i', lastChar);
    }

    @Test
    public void testReadLine() throws IOException {
        String lineOne = reader.readLine(); // Read "This is a test string"
        String lineTwo = reader.readLine(); // Read "Test string with"
        String lineThree = reader.readLine(); // Read "multiple lines"

        assertEquals("This is a test string", lineOne);
        assertEquals("Test string with", lineTwo);
        assertEquals("multiple lines", lineThree);
    }

    @Test
    public void testReadLineAtEOF() throws IOException {
        reader.readLine(); // Read "This is a test string"
        reader.readLine(); // Read "Test string with"
        reader.readLine(); // Read "multiple lines"
        String line = reader.readLine(); // Read "" (null)

        assertNull(line);
    }

    @Test
    public void testLookAhead() throws IOException {
        int charOne = reader.lookAhead(); // Look ahead first character 'T'
        int charTwo = reader.read(); // Read 'T'

        assertEquals('T', charOne);
        assertEquals('T', charTwo);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int lineNumberOne = reader.getLineNumber(); // Get line number 1
        reader.readLine(); // Read "This is a test string"
        int lineNumberTwo = reader.getLineNumber(); // Get line number 2

        assertEquals(0, lineNumberOne);
        assertEquals(1, lineNumberTwo);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        Reader r = new StringReader("");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(r);
        int character = reader.read(); // Read END_OF_STREAM

        assertEquals(-1, character);
    }
}