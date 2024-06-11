package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal strings.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] bufferOctal1 = new byte[]{'0', '1', '4', '4', '4', '4', '4'};
        long expectedOctal1 = 0114444L;
        long actualOctal1 = TarUtils.parseOctal(bufferOctal1, 0, bufferOctal1.length);
        assertEquals(expectedOctal1, actualOctal1);

        byte[] bufferOctal2 = new byte[]{'0', '1', '7', '7', '5', '5', '5', '5'};
        long expectedOctal2 = 017755555L;
        long actualOctal2 = TarUtils.parseOctal(bufferOctal2, 0, bufferOctal2.length);
        assertEquals(expectedOctal2, actualOctal2);
    }

    /**
     * Test parseOctal method with invalid octal strings.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] bufferOctalInvalid = new byte[]{'0', '1', 'a', '4', '4', '4', '4'};
        TarUtils.parseOctal(bufferOctalInvalid, 0, bufferOctalInvalid.length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] bufferName1 = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expectedName1 = "tar.file.gz";
        String actualName1 = TarUtils.parseName(bufferName1, 0, bufferName1.length);
        assertEquals(expectedName1, actualName1);

        byte[] bufferName2 = new byte[]{'./', '.', '.', 'a', '.', 'b', '.', 'c', '.', 'd', '.', 'e', 0};
        String expectedName2 = "././a/b/c/d/e/";
        String actualName2 = TarUtils.parseName(bufferName2, 0, bufferName2.length);
        assertEquals(expectedName2, actualName2);
    }

    /**
     * Test parseName method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseName_InvalidInput() {
        byte[] bufferNameInvalid = new byte[]{'0', '1', '2', '3', '4', '5', 0};
        TarUtils.parseName(bufferNameInvalid, 0, bufferNameInvalid.length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name1 = "tar.file.gz";
        byte[] expectedBufferName1 = new byte[]{'t', 'a', 'r', '.', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 0};
        byte[] actualBufferName1 = new byte[expectedBufferName1.length];
        int offset = TarUtils.formatNameBytes(name1, actualBufferName1, 0, expectedBufferName1.length);
        assertArrayEquals(expectedBufferName1, actualBufferName1);
        assertEquals(expectedBufferName1.length, offset);

        String name2 = "././a/b/c/d/e/";
        byte[] expectedBufferName2 = new byte[]{'./', '.', '.', 'a', '.', 'b', '.', 'c', '.', 'd', '.', 'e', 0};
        byte[] actualBufferName2 = new byte[expectedBufferName2.length];
        offset = TarUtils.formatNameBytes(name2, actualBufferName2, 0, expectedBufferName2.length);
        assertArrayEquals(expectedBufferName2, actualBufferName2);
        assertEquals(expectedBufferName2.length, offset);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value1 = 0x12345678L;
        byte[] expectedBuffer1 = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        byte[] actualBuffer1 = new byte[expectedBuffer1.length];
        TarUtils.formatUnsignedOctalString(value1, actualBuffer1, 0, expectedBuffer1.length);
        assertArrayEquals(expectedBuffer1, actualBuffer1);

        long value2 = 0xFFFFFFFFL;
        byte[] expectedBuffer2 = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        byte[] actualBuffer2 = new byte[expectedBuffer2.length];
        TarUtils.formatUnsignedOctalString(value2, actualBuffer2, 0, expectedBuffer2.length);
        assertArrayEquals(expectedBuffer2, actualBuffer2);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_InvalidInput() {
        long valueInvalid = 0x123456789L;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(valueInvalid, buffer, 0, 8);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value1 = 0x12345678L;
        byte[] expectedBuffer1 = new byte[]{'0', '1', '1', '2', '3', '4', '5', '6', '7', '8', ' ', ' ', '0'};
        byte[] actualBuffer1 = new byte[expectedBuffer1.length];
        int offset = TarUtils.formatOctalBytes(value1, actualBuffer1, 0, expectedBuffer1.length);
        assertArrayEquals(expectedBuffer1, actualBuffer1);
        assertEquals(expectedBuffer1.length, offset);

        long value2 = 0xFFFFFFFFL;
        byte[] expectedBuffer2 = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', ' ', '0'};
        byte[] actualBuffer2 = new byte[expectedBuffer2.length];
        offset = TarUtils.formatOctalBytes(value2, actualBuffer2, 0, expectedBuffer2.length);
        assertArrayEquals(expectedBuffer2, actualBuffer2);
        assertEquals(expectedBuffer2.length, offset);
    }

    /**
     * Test formatLongOctalBytes method with valid input.
     */
    @Test
    public void testFormatLongOctalBytes_ValidInput() {
        long value1 = 0x12345678L;
        byte[] expectedBuffer1 = new byte[]{'0', '1', '1', '2', '3', '4', '5', '6', '7', '8', ' ', ' '};
        byte[] actualBuffer1 = new byte[expectedBuffer1.length];
        int offset = TarUtils.formatLongOctalBytes(value1, actualBuffer1, 0, expectedBuffer1.length);
        assertArrayEquals(expectedBuffer1, actualBuffer1);
        assertEquals(expectedBuffer1.length, offset);

        long value2 = 0xFFFFFFFFL;
        byte[] expectedBuffer2 = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        byte[] actualBuffer2 = new byte[expectedBuffer2.length];
        offset = TarUtils.formatLongOctalBytes(value2, actualBuffer2, 0, expectedBuffer2.length);
        assertArrayEquals(expectedBuffer2, actualBuffer2);
        assertEquals(expectedBuffer2.length, offset);
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long checksum1 = 0x12345678L;
        byte[] expectedBuffer1 = new byte[]{'0', '1', '1', '2', '3', '4', '5', '6', '7', '8', ' ', ' '};
        byte[] actualBuffer1 = new byte[expectedBuffer1.length];
        int offset = TarUtils.formatCheckSumOctalBytes(checksum1, actualBuffer1, 0, expectedBuffer1.length);
        assertArrayEquals(expectedBuffer1, actualBuffer1);
        assertEquals(expectedBuffer1.length, offset);

        long checksum2 = 0xFFFFFFFFL;
        byte[] expectedBuffer2 = new byte[]{'0', '3', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', ' '};
        byte[] actualBuffer2 = new byte[expectedBuffer2.length];
        offset = TarUtils.formatCheckSumOctalBytes(checksum2, actualBuffer2, 0, expectedBuffer2.length);
        assertArrayEquals(expectedBuffer2, actualBuffer2);
        assertEquals(expectedBuffer2.length, offset);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n'};
        long checksumExpected = 0x25c5a27c1b9b2e1dL;
        long checksumActual = TarUtils.computeCheckSum(buffer);
        assertEquals(checksumExpected, checksumActual);
    }
}