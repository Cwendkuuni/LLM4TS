package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '3'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals("Expected result to be 017543", 017543L, result);

        byte[] invalidBuffer = new byte[]{'0', '7', '5', 'A', '4', '3'};
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
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '\u0000'};
        String expected = "tar.object.tar.";
        assertEquals("Expected result to be 'tar.object.tar.'", expected, TarUtils.parseName(buffer, 0, buffer.length));

        byte[] invalidBuffer = new byte[]{'t', 'a', 'r', '.', 'o', 'b', 'j', 'e', 'c', 't', '.', 't', 'a', 'r', '.', '\u0001'};
        try {
            TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] emptyBuffer = new byte[0];
        assertNull("Expected result to be null", TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.object.tar.";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals("Expected byte at position "+i+" to be '"+name.charAt(i)+"'", (byte) name.charAt(i), buffer[i]);
        }

        assertEquals("Expected offset to be offset+length", offset + buffer.length, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[8];

        long value = 12345;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '00216015'", new byte[]{'0', '0', '2', '1', '6', '0', '1', '5'}, buffer);

        long value2 = 0;
        TarUtils.formatUnsignedOctalString(value2, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '0'", buffer);

        long value3 = 65535;
        TarUtils.formatUnsignedOctalString(value3, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '77777'", buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 12345;
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '00216015  '", new byte[]{'0', '0', '2', '1', '6', '0', '1', '5', ' ', ' '}, buffer, 0, 8);
        assertEquals("Expected offset to be offset+length", offset + buffer.length, buffer.length);

        long value2 = 0;
        int offset2 = TarUtils.formatOctalBytes(value2, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '            '", new byte[10], buffer);
        assertEquals("Expected offset to be offset+length", offset2 + buffer.length, buffer.length);

        long value3 = 65535;
        int offset3 = TarUtils.formatOctalBytes(value3, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '77777  '", new byte[]{'7', '7', '7', '7', '7', '7', '7', '7', ' ', ' '}, buffer, 0, 9);
        assertEquals("Expected offset to be offset+length", offset3 + buffer.length, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[9];

        long value = 12345;
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '00216015'
", new byte[]{'0', '0', '2', '1', '6', '0', '1', '5'}, buffer, 0, 7);
        assertEquals("Expected offset to be offset+length", offset + buffer.length, buffer.length);

        long value2 = 0;
        int offset2 = TarUtils.formatLongOctalBytes(value2, buffer, 0, buffer.length);

        assertEquals("Expected result to be null", null, buffer);
        assertEquals("Expected offset to be offset+length", offset2 + buffer.length, buffer.length);

        long value3 = 65535;
        int offset3 = TarUtils.formatLongOctalBytes(value3, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '77777'", buffer, 0, 7);
        assertEquals("Expected offset to be offset+length", offset3 + buffer.length, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 12345;
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '0021601500'", new byte[]{'0', '0', '2', '1', '6', '0', '1', '5', '0', '0'}, buffer);
        assertEquals("Expected offset to be offset+length", offset + buffer.length, buffer.length);

        long value2 = 0;
        int offset2 = TarUtils.formatCheckSumOctalBytes(value2, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '0000000000'", new byte[10], buffer);
        assertEquals("Expected offset to be offset+length", offset2 + buffer.length, buffer.length);

        long value3 = 65535;
        int offset3 = TarUtils.formatCheckSumOctalBytes(value3, buffer, 0, buffer.length);

        assertArrayEquals("Expected result to be '7777777777'", new byte[]{'7', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer);
        assertEquals("Expected offset to be offset+length", offset3 + buffer.length, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', 'H', 'e', 'a', 'd', 'e', 'r', '\u0000'};
        long checksum = TarUtils.computeChecksum(buffer);

        long expectedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            expectedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }

        assertEquals("Expected checksum to be '"+Long.toHexString(expectedChecksum)+"'", Long.toHexString(expectedChecksum), Long.toHexString(checksum));
    }
}