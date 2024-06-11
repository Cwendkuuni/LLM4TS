import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        StringReader reader = new StringReader("Test\nString");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        try {
            int result = extendedBufferedReader.read();
            assertEquals('T', (char) result);
            result = extendedBufferedReader.read();
            assertEquals('e', (char) result);
            result = extendedBufferedReader.read();
            assertEquals('s', (char) result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadAgain() {
        StringReader reader = new StringReader("Test\nString");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        try {
            extendedBufferedReader.read();
            int result = extendedBufferedReader.readAgain();
            assertEquals('T', (char) result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadLine() {
        StringReader reader = new StringReader("Test\nString");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        try {
            String result = extendedBufferedReader.readLine();
            assertEquals("Test", result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLookAhead() {
        StringReader reader = new StringReader("Test\nString");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        try {
            int result = extendedBufferedReader.lookAhead();
            assertEquals('T', (char) result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLineNumber() {
        StringReader reader = new StringReader("Test\nString");
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        try {
            int result = extendedBufferedReader.getLineNumber();
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}