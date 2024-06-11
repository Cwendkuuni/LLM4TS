package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '5', (byte) '\u0000'};
        assertEquals(017555555L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '\u0000'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) '\u0000'};
        assertEquals("abc", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{};
        assertNull(TarUtils.parseName(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) '\u0000'};
        assertEquals("abc", TarUtils.parseName(buffer3, 0, 3));
        assertEquals("abcd", TarUtils.parseName(buffer3, 0, 4));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name";
        byte[] buffer = new byte[name.length() + 1];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("name", new String(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(123, buffer, 0, 10);
        assertEquals("123", new String(buffer, 0, 3));

        TarUtils.formatUnsignedOctalString(1234567, buffer, 0, 10);
        assertEquals("1234567", new String(buffer, 0, 7));

        TarUtils.formatUnsignedOctalString(1234567890, buffer, 0, 10);
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(1234567890, buffer, 0, 10));
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(1234567, buffer, 0, 13);
        assertEquals("123\u0020012\u0020015\u00200111\u0020", new String(buffer, 0, 12));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(1234567890123L, buffer, 0, 11);
        assertEquals("123\u0020012\u0020377151\u0020", new String(buffer, 0, 10));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[13];
        long checksum = 1234567890;
        TarUtils.formatCheckSumOctalBytes(checksum, buffer, 0, 13);
        assertEquals("123\u0020136\u00200112\u0020145\u0020251\u0020", new String(buffer, 0, 11));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(0x101 + 0x202 + 0x303 + 0x404 + 0x505, checksum);
    }
}