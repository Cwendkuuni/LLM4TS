package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{-65, 73, 68, 65, 61, 63, 63, 32, 66, 6F, 6F};
        assertEquals(207520L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{75, 73, 68, 65, 61, 63, 63, 62, 32, 66, 6F, 6F};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{75, 73, 68, 65, 61, 63, 63};
        assertEquals(20752L, TarUtils.parseOctal(buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[]{75, 73, 68, 65, 61, 63, 63, 32, 67};
        assertEquals(207521L, TarUtils.parseOctal(buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{75, 73, 68, 65, 61, 63, 63, 62, 65, 65, 65};
        assertEquals(41943040L, TarUtils.parseOctal(buffer5, 0, buffer5.length));

        byte[] buffer6 = new byte[]{75, 73, 68, 65, 61, 63, 63, 62, 65, 65, 65, 65};
        assertEquals(167772160L, TarUtils.parseOctal(buffer6, 0, buffer6.length));

        byte[] buffer7 = new byte[]{75, 73, 68, 65, 61, 63, 63, 62, 65, 65, 65, 65, 65};
        assertEquals(10737418240L, TarUtils.parseOctal(buffer7, 0, buffer7.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 0x00, 68, 65, 61, 64, 65, 72, 32, 63, 61, 6D, 65, 32, 6F, 66, 66, 65, 63, 74, 31, 0};
        assertEquals("headerName.data", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{(byte) 0x00, 68, 65, 61, 64, 65, 72, 32, 63, 61, 6D, 65};
        assertNull(TarUtils.parseName(buffer2, 0, buffer2.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "headerName.data";
        byte[] buffer = new byte[name.length() + 1];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        byte[] expected = new byte[]{68, 65, 61, 64, 65, 72, 32, 63, 61, 6D, 65, 32, 6F, 66, 66, 65, 63, 74, (byte) 0x00};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 20752L;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 20752L;
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 32, 0};
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[13];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 32, 0}; // trailing space and null
        assertArrayEquals(expected, buffer);
    }
}