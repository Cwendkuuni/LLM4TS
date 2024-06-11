package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);

        byte[] invalidBuffer = new byte[]{(byte) '0', (byte) '1', (byte) '8', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'};
        try {
            TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] emptyBuffer = new byte[0];
        try {
            TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] nameBuffer = new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) '.', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        String result = TarUtils.parseName(nameBuffer, 0, nameBuffer.length);
        assertEquals("foo.bar.compress.zip.tar.gz", result);

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo.bar.compress.zip.tar.gz";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) '.', (byte) 'b', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'o', (byte) 'm', (byte) 'p', (byte) 'r', (byte) 'e', (byte) 's', (byte) 's', (byte) '.', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'}, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345678L;
        byte[] buffer = new byte[15];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345678L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) ' ', (byte) '0'}, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345678901234567890L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9'}, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345678901234567890L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '0', (byte) ' ', (byte) '0'}, buffer, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(354871756711654L, result);
    }
}