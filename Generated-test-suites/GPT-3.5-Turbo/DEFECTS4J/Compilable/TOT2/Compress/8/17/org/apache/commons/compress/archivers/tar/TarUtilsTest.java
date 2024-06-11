package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_basic() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, 7);
        assertEquals(1234567, result);
    }

    @Test
    public void testParseOctal_missingTrailingSpace() {
        byte[] buffer = {'1', '2', '3', '4', '5', '6'};
        try {
            TarUtils.parseOctal(buffer, 0, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testParseName_basic() {
        byte[] buffer = {'e', 'x', 'a', 'm', 'p', 'l', 'e', 0, 't', 'e', 's', 't'};
        String result = TarUtils.parseName(buffer, 0, 12);
        assertEquals("example", result);
    }

    @Test
    public void testParseName_missingTrailingNull() {
        byte[] buffer = {'t', 'e', 's', 't', '1', '2', '3', '4', '5', '6', '7', '8'};
        String result = TarUtils.parseName(buffer, 0, 12);
        assertEquals("test1234567", result);
    }

    @Test
    public void testFormatNameBytes_basic() {
        String name = "test";
        byte[] buffer = new byte[10];
        int updatedOffset = TarUtils.formatNameBytes(name, buffer, 0, 10);
        assertEquals(10, updatedOffset);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        for (int i = 4; i < 10; i++) {
            assertEquals(0, buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
        assertArrayEquals(new byte[] {'0', '0', '1', '7', 0}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[5];
        int updatedOffset = TarUtils.formatOctalBytes(123, buffer, 0, 5);
        assertEquals(5, updatedOffset);
        assertArrayEquals(new byte[] {'0', '0', '1', '7', ' '}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'t', 'e', 's', 't', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(764, result);
    }
}