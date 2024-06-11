package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '8', (byte) '3', (byte) '4', (byte) '5', (byte) '6'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));

        byte[] trailingNullBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) 0};
        assertEquals(0123456L, TarUtils.parseOctal(trailingNullBuffer, 0, trailingNullBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) '.', (byte) '.', (byte) '1', (byte) '2', (byte) '3', (byte) '4'};
        assertEquals("abcde..1234", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));

        byte[] invalidBuffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) '.', (byte) '.', (byte) '1', (byte) '2', (byte) '3', (byte) '5'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length));

        byte[] trailingNullBuffer = new byte[]{(byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) '.', (byte) '.', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) 0};
        assertEquals("abcde..1234", TarUtils.parseName(trailingNullBuffer, 0, trailingNullBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcde";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("abcde", new String(buffer, 0, offset));

        byte[] emptyNameBuffer = new byte[10];
        int emptyOffset = TarUtils.formatNameBytes("", emptyNameBuffer, 0, emptyNameBuffer.length);
        assertEquals(0, emptyOffset);

        byte[] tooLongNameBuffer = new byte[5];
        int tooLongOffset = TarUtils.formatNameBytes("abcdefg", tooLongNameBuffer, 0, tooLongNameBuffer.length);
        assertEquals(5, tooLongOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0123456L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'}, buffer, 0, offset);

        long value2 = 0;
        byte[] buffer2 = new byte[10];
        int offset2 = TarUtils.formatUnsignedOctalString(value2, buffer2, 0, buffer2.length);
        assertEquals(0, offset2);

        long value3 = 0777777L;
        byte[] buffer3 = new byte[10];
        int offset3 = TarUtils.formatUnsignedOctalString(value3, buffer3, 0, buffer3.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1'}, buffer3, 0, offset3);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0123456L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long value2 = 0;
        byte[] buffer2 = new byte[10];
        int offset2 = TarUtils.formatOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(0, offset2);

        long value3 = 0777777L;
        byte[] buffer3 = new byte[10];
        int offset3 = TarUtils.formatOctalBytes(value3, buffer3, 0, buffer3.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) ' ', (byte) '0'}, buffer3, 0, offset3);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0123456L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'}, buffer, 0, offset);

        long value2 = 0;
        byte[] buffer2 = new byte[10];
        int offset2 = TarUtils.formatLongOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(0, offset2);

        long value3 = 0777777L;
        byte[] buffer3 = new byte[10];
        int offset3 = TarUtils.formatLongOctalBytes(value3, buffer3, 0, buffer3.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1'}, buffer3, 0, offset3);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0123456L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '0', (byte) ' ', (byte) '0'}, buffer, 0, offset);

        long value2 = 0;
        byte[] buffer2 = new byte[10];
        int offset2 = TarUtils.formatCheckSumOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(0, offset2);

        long value3 = 0777777L;
        byte[] buffer3 = new byte[10];
        int offset3 = TarUtils.formatCheckSumOctalBytes(value3, buffer3, 0, buffer3.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '1', (byte) '0', (byte) ' ', (byte) '0'}, buffer3, 0, offset3);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r', (byte) ' ', (byte) 't', (byte) 'a', (byte) 'r', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) ' ', (byte) 'c', (byte) 'h', (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm'};
        long checksum = TarUtils.computeCheckSum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }
}