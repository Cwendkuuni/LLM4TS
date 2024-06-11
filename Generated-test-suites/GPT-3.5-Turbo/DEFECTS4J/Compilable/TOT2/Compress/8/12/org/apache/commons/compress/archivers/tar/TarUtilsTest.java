package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = {' ', ' ', '1', '2', '3', '\0', ' '};
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(123L, result);
    }

    @Test
    public void testParseOctalMissingTrailing() {
        byte[] buffer = {' ', ' ', '1', '2', '3', '\0'};
        try {
            TarUtils.parseOctal(buffer, 0, 6);
            fail("Missing trailing space/NUL not detected");
        } catch (IllegalArgumentException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testParseNameValidInput() {
        byte[] buffer = {'t', 'e', 's', 't', '\0', ' ', ' '};
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[5];
        int result = TarUtils.formatNameBytes("test", buffer, 0, 5);
        assertArrayEquals(new byte[] {'t', 'e', 's', 't', 0}, buffer);
        assertEquals(5, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(123L, buffer, 0, 3);
        assertArrayEquals(new byte[] {'1', '2', '3'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];
        int result = TarUtils.formatOctalBytes(123L, buffer, 0, 4);
        assertArrayEquals(new byte[] {'1', '2', '3', ' '}, buffer);
        assertEquals(4, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] {1, 2, 3};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(6L, result);
    }

}