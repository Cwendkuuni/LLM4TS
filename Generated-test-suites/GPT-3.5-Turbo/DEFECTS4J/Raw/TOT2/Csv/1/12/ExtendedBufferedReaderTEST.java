import static org.junit.Assert.*;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    ExtendedBufferedReader reader;

    @Test
    public void testRead() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Test");
            reader = new ExtendedBufferedReader(sr);

            assertEquals('T', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testReadAgain() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Test");
            reader = new ExtendedBufferedReader(sr);

            assertEquals(-2, reader.readAgain()); // No character read yet

            // Read a character
            reader.read();
            assertEquals('T', reader.readAgain());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testReadWithBuffer() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Test\nLine\rAnother");
            reader = new ExtendedBufferedReader(sr);

            char[] buf = new char[10];
            reader.read(buf, 0, 4);
            assertEquals("Test", new String(buf, 0, 4));

            reader.read(buf, 0, 10);
            assertEquals("Line", new String(buf, 0, 4));

            reader.read(buf, 0, 10);
            assertEquals("Another", new String(buf, 0, 7));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testReadLine() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Line 1\nLine 2\r\nLine 3\r");
            reader = new ExtendedBufferedReader(sr);

            assertEquals("Line 1", reader.readLine());
            assertEquals(1, reader.getLineNumber());

            assertEquals("Line 2", reader.readLine());
            assertEquals(2, reader.getLineNumber());

            assertEquals("Line 3", reader.readLine());
            assertEquals(3, reader.getLineNumber());

            assertNull(reader.readLine()); // EOF
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testLookAhead() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Test");
            reader = new ExtendedBufferedReader(sr);

            assertEquals('T', reader.lookAhead());
            assertEquals('T', reader.read());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testGetLineNumber() {
        try {
            // Create ExtendedBufferedReader instance with a StringReader
            StringReader sr = new StringReader("Line 1\nLine 2\r\nLine 3\r");
            reader = new ExtendedBufferedReader(sr);

            reader.readLine(); // Line 1
            assertEquals(1, reader.getLineNumber());

            reader.readLine(); // Line 2
            assertEquals(2, reader.getLineNumber());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}