import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        Reader mockReader = new StringReader("This is a test\nMultiple\nLines");
        reader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    public void testRead() throws IOException {
        int expectedChar = 'T';
        int actualChar = reader.read();
        assertEquals(expectedChar, actualChar);

        expectedChar = 'h';
        actualChar = reader.read();
        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testReadAgain() throws IOException {
        int expectedChar = 'T';
        reader.read();
        int actualChar = reader.readAgain();
        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        int expectedLength = 5;
        int actualLength = reader.read(buf, 0, 5);
        assertEquals(expectedLength, actualLength);

        char[] expectedChars = {' ', 'i', 's', ' ', 'a'};
        char[] actualChars = new char[5];
        System.arraycopy(buf, 0, actualChars, 0, 5);
        assertArrayEquals(expectedChars, actualChars);
    }

    @Test
    public void testReadLine() throws IOException {
        String expectedLine1 = "This is a test";
        String actualLine1 = reader.readLine();
        assertEquals(expectedLine1, actualLine1);

        String expectedLine2 = "Multiple";
        String actualLine2 = reader.readLine();
        assertEquals(expectedLine2, actualLine2);
    }

    @Test
    public void testLookAhead() throws IOException {
        int expectedChar = 'h';
        int actualChar = reader.lookAhead();
        assertEquals(expectedChar, actualChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int expectedLine = 0;
        int actualLine = reader.getLineNumber();
        assertEquals(expectedLine, actualLine);

        reader.readLine();

        expectedLine = 1;
        actualLine = reader.getLineNumber();
        assertEquals(expectedLine, actualLine);
    }
}