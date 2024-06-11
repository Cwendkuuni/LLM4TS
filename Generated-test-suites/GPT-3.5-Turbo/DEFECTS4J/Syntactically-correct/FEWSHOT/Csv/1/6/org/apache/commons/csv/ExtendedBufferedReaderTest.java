import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test when there are characters in the reader
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        try {
            assertEquals((int) 'a', reader.read());
            assertEquals((int) 'b', reader.read());
            assertEquals((int) 'c', reader.read());
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        // Test when the end of stream is reached
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            assertEquals(-1, reader.read());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testReadAgain() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        try {
            reader.read();
            reader.read();
            assertEquals((int) 'b', reader.readAgain());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testReadCharArray() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        char[] buf = new char[2];
        try {
            assertEquals(2, reader.read(buf, 0, 2));
            assertEquals('a', buf[0]);
            assertEquals('b', buf[1]);
            assertEquals(1, reader.read(buf, 0, 2));
            assertEquals('c', buf[0]);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testReadLine() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\n"));
        try {
            assertEquals("line1", reader.readLine());
            assertEquals("line2", reader.readLine());
            assertNull(reader.readLine());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testLookAhead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        try {
            assertEquals((int) 'a', reader.lookAhead());
            reader.read();
            assertEquals((int) 'b', reader.lookAhead());
            reader.read();
            assertEquals((int) 'c', reader.lookAhead());
            reader.read();
            assertEquals(-1, reader.lookAhead());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\n"));
        try {
            assertEquals(0, reader.getLineNumber());
            reader.readLine();
            assertEquals(1, reader.getLineNumber());
            reader.readLine();
            assertEquals(2, reader.getLineNumber());
            reader.readLine();
            assertEquals(2, reader.getLineNumber());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
}