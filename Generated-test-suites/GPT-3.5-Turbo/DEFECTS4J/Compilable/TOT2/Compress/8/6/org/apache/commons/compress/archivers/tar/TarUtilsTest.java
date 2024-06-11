package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {1, 0, 0, 0}; // valid octal string "100"
        long expected = 64; // 100 in octal
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'t', 'e', 's', 't', 0}; // valid name "test"
        String expected = "test";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "example";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String expected = "example\0\0\0\0\0";
        String result = new String(buffer, 0, newOffset);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 75; // value in octal is 113
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "0113";
        String result = new String(buffer, 0, 4);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5, 6}; // dummy header buffer
        long expected = 21; // sum of all elements
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testInvalidOctalString() {
        byte[] buffer = {'a', 'b', 'c'}; // invalid octal string as it contains non-octal characters
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // as expected
        }
    }

    @Test
    public void testInvalidName() {
        byte[] buffer = {1, 2, 3, 4}; // invalid name as it does not contain a null terminator
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // as expected
        }
    }

    // Additional test cases for edge cases and error scenarios can be added here

}