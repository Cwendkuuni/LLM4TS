package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{-67, 75, 71, 72, 65, 72, 72, 65, 63, 68, 61, 72, 0};
        assertEquals(01751014010, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] invalidBuffer = new byte[]{71, 73, 73, 0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{68, 65, 61, 74, 65, 72, 6E, 61, 74, 69, 6F, 6E, 0};
        assertEquals("Apple\n", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[]{0};
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Apple\n";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{68, 65, 61, 74, 65, 72, 6E, 61, 74, 69, 6F, 0, 0, 0, 0, 0, 0}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 4, 5, 1, 2, 3}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, ' ', 0, 0, 4, 5, 1, 2, 3, 0}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 0, 0}, buffer);
        assertEquals(12, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890123L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0}, buffer);
        assertEquals(11, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] header = new byte[]{65, 0, 78, 0, 73, 73, 73, 0, 68, 65, 61, 64, 65, 72, 0};
        long checksum = TarUtils.computeCheckSum(header);
        assertEquals(26178L, checksum);
    }
}