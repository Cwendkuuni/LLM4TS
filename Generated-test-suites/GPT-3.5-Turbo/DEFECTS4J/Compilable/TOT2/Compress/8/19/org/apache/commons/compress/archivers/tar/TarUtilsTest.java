package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = {'1', '2', '3', '4', '5', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(012345L, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'T', 'e', 's', 't', 0, 'E', 'n', 'd'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("Test", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Test";
        byte[] buffer = new byte[8];
        int updatedOffset = TarUtils.formatNameBytes(name, buffer, 0, 8);
        assertEquals(8, updatedOffset);
        assertArrayEquals(new byte[] {'T', 'e', 's', 't', 0, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
        assertArrayEquals(new byte[] {'3', '0', '1', '7', '1'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[8];
        int updatedOffset = TarUtils.formatOctalBytes(value, buffer, 0, 8);
        assertEquals(8, updatedOffset);
        assertArrayEquals(new byte[] {'3', '0', '1', '7', '1', ' ', 0, 0}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {1, 2, 3, 4, 5};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(15, result);
    }

}