import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test when there are characters to read
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        try {
            int result = reader.read();
            assertEquals('H', result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadAgain() {
        // Test when no character has been read yet
        ExtendedBufferedReader reader1 = new ExtendedBufferedReader(new StringReader("Hello World"));
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader1.readAgain());

        // Test when a character has been read
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader("Hello World"));
        try {
            reader2.read();
            int result = reader2.readAgain();
            assertEquals('H', result);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test when the end of stream was reached
        ExtendedBufferedReader reader3 = new ExtendedBufferedReader(new StringReader("Hello World"));
        try {
            while (reader3.read() != ExtendedBufferedReader.END_OF_STREAM) {
                // Continue reading
            }
            int result = reader3.readAgain();
            assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadCharArray() {
        // Test when there are characters to read
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        char[] buffer = new char[5];
        try {
            int result = reader.read(buffer, 0, 5);
            assertEquals(5, result);
            assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test when there are no characters to read
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader("Hello World"));
        char[] buffer2 = new char[0];
        try {
            int result = reader2.read(buffer2, 0, 0);
            assertEquals(0, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadLine() {
        // Test when there is a line to read
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        try {
            String result = reader.readLine();
            assertEquals("Hello", result);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test when there is no line to read
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader(""));
        try {
            String result = reader2.readLine();
            assertNull(result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testLookAhead() {
        // Test when there is a character to look ahead
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        try {
            int result = reader.lookAhead();
            assertEquals('H', result);
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test when there is no character to look ahead
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader(""));
        try {
            int result = reader2.lookAhead();
            assertEquals(-1, result);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testGetLineNumber() {
        // Test when no line has been read
        ExtendedBufferedReader reader1 = new ExtendedBufferedReader(new StringReader(""));
        assertEquals(0, reader1.getLineNumber());

        // Test when lines have been read
        ExtendedBufferedReader reader2 = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        try {
            reader2.readLine();
            reader2.readLine();
        } catch (IOException e) {
            fail("IOException thrown");
        }
        assertEquals(2, reader2.getLineNumber());
    }
}