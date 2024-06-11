import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        int expectedChar = 'H';
        int actualChar;

        actualChar = reader.read();
        assertEquals("Expected 'H' but got " + (char) actualChar, expectedChar, actualChar);

        // Test reading all characters in the input string
        int i = 0;
        while (reader.read() != -1) {
            i++;
        }
        assertEquals("Expected to read 11 characters but read " + i, 11, i);
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A"));
        int expectedChar = 'A';
        int actualChar;

        actualChar = reader.read();
        assertEquals("Expected 'A' but got " + (char) actualChar, expectedChar, actualChar);
        actualChar = reader.readAgain();
        assertEquals("Expected 'A' but got " + (char) actualChar, expectedChar, actualChar);
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        String expectedLine = "Hello";
        String actualLine;

        actualLine = reader.readLine();
        assertEquals("Expected '" + expectedLine + "' but got '" + actualLine + "'", expectedLine, actualLine);
    }

    @Test(expected = IOException.class)
    public void testReadBeyondEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read(); // Read the empty character at the beginning of the stream
        reader.read(); // Expect an IOException to be thrown
    }

    @Test
    public void testReadEmptyReader() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int actualChar = reader.read();
        assertEquals("Expected -1 but got " + actualChar, -1, actualChar);
    }

    @Test(expected = IOException.class)
    public void testReadInvalidCharacters() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\u0000")); // Null character
        reader.read(); // Expect an IOException to be thrown
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ABC"));
        int expectedChar = 'A';
        int actualChar;

        actualChar = reader.lookAhead();
        assertEquals("Expected 'A' but got " + (char) actualChar, expectedChar, actualChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2\nLine 3"));
        int expectedLineNumber = 1;
        int actualLineNumber;

        reader.readLine(); // Read the first line
        reader.readLine(); // Read the second line

        actualLineNumber = reader.getLineNumber();
        assertEquals("Expected line number 3 but got " + actualLineNumber, expectedLineNumber, actualLineNumber);
    }
}