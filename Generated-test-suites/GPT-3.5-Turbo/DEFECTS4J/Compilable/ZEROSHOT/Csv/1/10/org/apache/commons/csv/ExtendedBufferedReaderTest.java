package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(null);
    }

    @Test
    public void testRead() {
        try {
            int result = reader.read();
            assertEquals(-1, result);
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadAgain() {
        int result = reader.readAgain();
        assertEquals(-2, result);
    }

    @Test
    public void testReadCharArray() {
        char[] buf = new char[10];
        try {
            int result = reader.read(buf, 0, 10);
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadLine() {
        try {
            String result = reader.readLine();
            assertNull(result);
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    @Test
    public void testLookAhead() {
        try {
            int result = reader.lookAhead();
            assertEquals(-1, result);
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetLineNumber() {
        int result = reader.getLineNumber();
        assertEquals(0, result);
    }

}