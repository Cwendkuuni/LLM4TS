package org.apache.commons.compress.archivers.tar;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TarUtilsTest {

    // Test case for parseOctal method
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '5', '5', '7', '7', '\u0000'};
        long expected = 01755775577L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '8', '5', '5', '7', '7', '\u0000'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    // Test case for parseName method
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '\u0000'};
        String expected = "tar.file.";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    // Test case for formatNameBytes method
    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();
        offset = TarUtils.formatNameBytes(name, buffer, offset, length);
        String result = new String(buffer, 0, offset);
        assertEquals(name, result);
    }

    // Test case for formatUnsignedOctalString method
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345678;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "012364074";
        String result = new String(buffer, 0, length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringOutOfBuffer() {
        long value = 1234567890;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    // Test case for formatOctalBytes method
    @Test
    public void testFormatOctalBytes() {
        long value = 12345678;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 10;
        offset = TarUtils.formatOctalBytes(value, buffer, offset, length);
        String expected = "012364074 ";
        String result = new String(buffer, 0, offset);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesOutOfBuffer() {
        long value = 1234567890;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    // Test case for computeCheckSum method
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x75, 0x72, 0x6e, 0x63, 0x68, 0x65, 0x64, 0x00, 0x66, 0x6f, 0x72, 0x73, 0x65, 0x74, 0x00}; 
        long checksum = 0x6d8561239b6f2916L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(checksum, result);
    }
}