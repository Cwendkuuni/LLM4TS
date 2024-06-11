import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadLine() {
        // Typical case
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        try {
            assertEquals("Hello", reader.readLine());
            assertEquals("World", reader.readLine());
            assertNull(reader.readLine());
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testReadAgain() {
        // Edge case
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Test"));
        try {
            reader.read();
            assertEquals('T', reader.readAgain());
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testRead() {
        // Error scenario
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Test"));
        try {
            assertEquals('T', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testReadWithBuffer() {
        // Typical case
        char[] buf = new char[5];
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Buffer\nTest"));
        try {
            assertEquals(6, reader.read(buf, 0, 6));
            assertEquals('B', buf[0]);
            assertEquals('u', buf[1]);
            assertEquals('f', buf[2]);
            assertEquals('f', buf[3]);
            assertEquals('e', buf[4]);
            assertEquals('r', buf[5]);
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testLookAhead() {
        // Edge case
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Test"));
        try {
            assertEquals('e', reader.lookAhead());
            assertEquals('T', reader.read());
            assertEquals('e', reader.read());
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testGetLineNumber() {
        // Typical case
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line1\nLine2\n"));
        try {
            reader.readLine();
            reader.readLine();
            assertEquals(2, reader.getLineNumber());
        } catch (IOException e) {
            // Handle exception
        }
    }

}