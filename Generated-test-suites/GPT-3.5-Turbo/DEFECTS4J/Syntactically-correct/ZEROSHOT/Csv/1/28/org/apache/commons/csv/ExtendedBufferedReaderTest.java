import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!";
        Reader reader = new StringReader(testData);

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Read the first character from the ExtendedBufferedReader
        int actual = extendedBufferedReader.read();

        // Assert that the actual character matches the expected character
        int expected = 'H';
        assertEquals(expected, actual);
    }

    @Test
    public void testReadAgain() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!";
        Reader reader = new StringReader(testData);

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Read the first character from the ExtendedBufferedReader
        extendedBufferedReader.read();
        int actual = extendedBufferedReader.readAgain();

        // Assert that the actual character matches the expected character
        int expected = 'H';
        assertEquals(expected, actual);
    }

    @Test
    public void testReadCharArray() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!";
        Reader reader = new StringReader(testData);
        char[] buf = new char[5];

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Read characters into the char array from the ExtendedBufferedReader
        int actual = extendedBufferedReader.read(buf, 0, 5);

        // Assert that the actual number of characters read matches the expected number
        int expected = 5;
        assertEquals(expected, actual);

        // Assert that the characters read match the expected characters
        char[] expectedBuf = {'H', 'e', 'l', 'l', 'o'};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testReadLine() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!\nThis is a test";
        Reader reader = new StringReader(testData);

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Read a line from the ExtendedBufferedReader
        String actual = extendedBufferedReader.readLine();

        // Assert that the actual line matches the expected line
        String expected = "Hello, world!";
        assertEquals(expected, actual);
    }

    @Test
    public void testLookAhead() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!";
        Reader reader = new StringReader(testData);

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Look ahead to the next character in the ExtendedBufferedReader
        int actual = extendedBufferedReader.lookAhead();

        // Assert that the actual character matches the expected character
        int expected = 'e';
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        // Create a Reader with some test data
        String testData = "Hello, world!\nThis is a test";
        Reader reader = new StringReader(testData);

        // Create an ExtendedBufferedReader with the Reader
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        // Read a line from the ExtendedBufferedReader
        extendedBufferedReader.readLine();

        // Get the line number from the ExtendedBufferedReader
        int actual = extendedBufferedReader.getLineNumber();

        // Assert that the actual line number matches the expected line number
        int expected = 1;
        assertEquals(expected, actual);
    }
}