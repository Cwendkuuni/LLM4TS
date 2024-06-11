import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test reading a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        try {
            int ch = reader.read();
            assertEquals('a', ch);
            assertEquals('a', reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test reading multiple characters
        reader = new ExtendedBufferedReader(new StringReader("hello"));
        try {
            int ch = reader.read();
            assertEquals('h', ch);
            assertEquals('h', reader.readAgain());
            
            ch = reader.read();
            assertEquals('e', ch);
            assertEquals('e', reader.readAgain());

            ch = reader.read();
            assertEquals('l', ch);
            assertEquals('l', reader.readAgain());
            
            ch = reader.read();
            assertEquals('l', ch);
            assertEquals('l', reader.readAgain());

            ch = reader.read();
            assertEquals('o', ch);
            assertEquals('o', reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test end of stream
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int ch = reader.read();
            assertEquals(-1, ch);
            assertEquals(-1, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadLine() {
        // Test reading a single line with newline character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\n"));
        try {
            String line = reader.readLine();
            assertEquals("Hello", line);
            assertEquals('\n', reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test reading multiple lines
        reader = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2\nLine 3"));
        try {
            String line = reader.readLine();
            assertEquals("Line 1", line);
            assertEquals('\n', reader.readAgain());

            line = reader.readLine();
            assertEquals("Line 2", line);
            assertEquals('\n', reader.readAgain());

            line = reader.readLine();
            assertEquals("Line 3", line);
            assertEquals(-1, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test reading a line with carriage return character
        reader = new ExtendedBufferedReader(new StringReader("Hello\r"));
        try {
            String line = reader.readLine();
            assertEquals("Hello", line);
            assertEquals('\r', reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test reading an empty line
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            String line = reader.readLine();
            assertNull(line);
            assertEquals(-1, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testLookAhead() {
        // Test looking ahead at a single character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        try {
            int ch = reader.lookAhead();
            assertEquals('a', ch);
            assertEquals(-2, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test looking ahead at multiple characters
        reader = new ExtendedBufferedReader(new StringReader("hello"));
        try {
            int ch = reader.lookAhead();
            assertEquals('h', ch);
            assertEquals(-2, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test looking ahead at the end of stream
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int ch = reader.lookAhead();
            assertEquals(-1, ch);
            assertEquals(-2, reader.readAgain());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetLineNumber() {
        // Test getting the line number after reading a single line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        try {
            reader.readLine();
            int lineNumber = reader.getLineNumber();
            assertEquals(1, lineNumber);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test getting the line number after reading multiple lines
        reader = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2\nLine 3"));
        try {
            reader.readLine();
            int lineNumber = reader.getLineNumber();
            assertEquals(1, lineNumber);

            reader.readLine();
            lineNumber = reader.getLineNumber();
            assertEquals(2, lineNumber);

            reader.readLine();
            lineNumber = reader.getLineNumber();
            assertEquals(3, lineNumber);
        } catch (IOException e) {
            fail("IOException occurred");
        }

        // Test getting the line number before reading any lines
        reader = new ExtendedBufferedReader(new StringReader(""));
        int lineNumber = reader.getLineNumber();
        assertEquals(0, lineNumber);
    }
}