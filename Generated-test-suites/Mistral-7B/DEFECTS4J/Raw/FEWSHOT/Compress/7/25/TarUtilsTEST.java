package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '5', '1'};
        assertEquals(01754051L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{'0', '1', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'1', '2', '3'};
        assertEquals(01123L, TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'0', '7', '7', '7'};
        assertEquals(0177777L, TarUtils.parseOctal(buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[]{'0'};
        assertEquals(0L, TarUtils.parseOctal(buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{' '};
        assertEquals(0L, TarUtils.parseOctal(buffer5, 0, buffer5.length));

        byte[] buffer6 = new byte[]{0};
        assertEquals(0L, TarUtils.parseOctal(buffer6, 0, buffer6.length));

        byte[] buffer7 = new byte[]{'1', '2'};
        assertEquals(012L, TarUtils.parseOctal(buffer7, 0, buffer7.length));

        byte[] buffer8 = new byte[]{'1', '2', '3', '4', '5', '6', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer8, 0, buffer8.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', 0};
        assertEquals("Tar Header", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{' ', ' ', ' ', 0};
        assertNull(TarUtils.parseName(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', ' ', 'F', 'i', 'l', 'e', ' ', '1', '.', '0', '0', 0};
        assertEquals("Tar File 1.0", TarUtils.parseName(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', '-', 'v', '1', '.', '0', '0', ' ', 'B', 'i', 'n', 'a', 'r', 'y', 0};
        assertEquals("Tar Header-binary Tar.bin", TarUtils.parseName(buffer3, 0, buffer3.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Tar Header";
        byte[] expected = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', 0};
        byte[] buffer = new byte[expected.length];
        int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(actual, buffer.length);

        name = "Tar File 1.0";
        expected = new byte[]{'T', 'a', 'r', ' ', 'F', 'i', 'l', 'e', ' ', '1', '.', '0', '0', 0};
        buffer = new byte[expected.length];
        actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(actual, buffer.length);

        name = "Tar Header-binary Tar.bin";
        expected = new byte[]{'T', 'a', 'r', ' ', 'H', 'e', 'a', 'd', 'e', 'r', '-', 'b', 'i', 'n', 'a', 'r', 'y', ' ', 'T', 'a', 'r', '.', 'b', 'i', 'n', 0};
        buffer = new byte[expected.length];
        actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(actual, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5'}, buffer);

        value = -12345;
        buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[10], buffer);

        value = 123456789;
        buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8'}, buffer);

        value = -123456789;
        buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[11], buffer);

        value = 1234567890;
        buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8'}, buffer);

        value = 0;
        buffer = new byte[1];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[9];
        int actual = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', ' ', '0'}, buffer);
        assertEquals(actual, buffer.length);

        value = -12345;
        buffer = new byte[9];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));

        value = 123456789;
        buffer = new byte[11];
        actual = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8', ' ', '0'}, buffer);
        assertEquals(actual, buffer.length);

        value = -123456789;
        buffer = new byte[11];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));

        value = 1234567890;
        buffer = new byte[12];
        actual = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8'}, buffer);
        assertEquals(actual, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[8];
        int actual = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5'}, buffer);
        assertEquals(actual, buffer.length);

        value = -12345;
        buffer = new byte[8];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length));

        value = 123456789;
        buffer = new byte[10];
        actual = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8'}, buffer);
        assertEquals(actual, buffer.length);

        value = -123456789;
        buffer = new byte[10];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length));

        value = 1234567890;
        buffer = new byte[11];
        actual = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5'}, buffer);
        assertEquals(actual, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int actual = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', ' ', '0'}, buffer);
        assertEquals(actual, buffer.length);

        value = -12345;
        buffer = new byte[10];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length));

        value = 123456789;
        buffer = new byte[11];
        actual = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5', '6', '7', '8'}, buffer);
        assertEquals(actual, buffer.length);

        value = -123456789;
        buffer = new byte[11];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length));

        value = 1234567890;
        buffer = new byte[12];
        actual = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '5'}, buffer);
        assertEquals(actual, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x75, 0x4e, 0x4e, 0x42, 0x61, 0x74, 0x65, 0x54, 0x79, 0x70, 0x65, 0x31, 0x33, 0x33};
        assertEquals(0x754e4e42L, TarUtils.computeCheckSum(buffer));

        byte[] buffer1 = new byte[]{0x0, 0x0};
        assertEquals(0L, TarUtils.computeCheckSum(buffer1));

        byte[] buffer2 = new byte[]{0};
        assertEquals(0L, TarUtils.computeCheckSum(buffer2));
    }
}