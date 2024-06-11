package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAXID;
import static org.apache.commons.compress.archivers.tar.TarConstants.MAXSIZE;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test for parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(01777777L, result);

        buffer = new byte[]{'0', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(01234567777L, result);

        buffer = new byte[]{'0', '1', '0'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'\0'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(0L, result);

        buffer = new byte[]{'1', '2', '3'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '\r', '\n'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'0', '7'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for parseOctalOrBinary method.
     */
    @Test
    public void testParseOctalOrBinary() {
        byte[] bufferOctal = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '0'};
        long result = TarUtils.parseOctalOrBinary(bufferOctal, 0, bufferOctal.length);
        Assert.assertEquals(01777777L, result);

        byte[] bufferBinary = new byte[]{'\u0000', (byte) 0x81, (byte) 0x12, (byte) 0x34};
        result = TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
        Assert.assertEquals(0x00001234L, result);

        bufferBinary = new byte[]{'\u0000', (byte) 0x80, (byte) 0x80};
        try {
            TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        bufferBinary = new byte[]{'\u0000'};
        result = TarUtils.parseOctalOrBinary(bufferBinary, 0, bufferBinary.length);
        Assert.assertEquals(0L, result);
    }

    /**
     * Test for parseBoolean method.
     */
    @Test
    public void testParseBoolean() {
        byte[] bufferTrue = new byte[]{'\u0001'};
        boolean result = TarUtils.parseBoolean(bufferTrue, 0);
        Assert.assertTrue(result);

        byte[] bufferFalse = new byte[]{'0'};
        result = TarUtils.parseBoolean(bufferFalse, 0);
        Assert.assertFalse(result);

        byte[] bufferTrailingSpaces = new byte[]{'0', ' ', ' ', ' '};
        result = TarUtils.parseBoolean(bufferTrailingSpaces, 0);
        Assert.assertFalse(result);

        byte[] bufferLeadingNUL = new byte[]{'\\0', '1'};
        result = TarUtils.parseBoolean(bufferLeadingNUL, 0);
        Assert.assertTrue(result);
    }

    /**
     * Test for parseName method.
     */
    @Test
    public void testParseName() {
        byte[] bufferName = new byte[]{'a', 'b', 'c', 'd', 'e', '\\0'};
        String result = TarUtils.parseName(bufferName, 0, bufferName.length);
        Assert.assertEquals("abcde", result);

        bufferName = new byte[]{'a', 'b', 'c', 'd', 'e', '\u0001', 'f'};
        result = TarUtils.parseName(bufferName, 0, bufferName.length);
        Assert.assertEquals("abcde", result);

        bufferName = new byte[]{'a', 'b', 'c', 'd', '\u0000'};
        try {
            TarUtils.parseName(bufferName, 0, bufferName.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "myEntryName";
        byte[] buffer = new byte[TarConstants.MAXID + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        Assert.assertEquals("myEntryName", result);

        name = "myEntryName\u0000";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        result = new String(buffer, 0, offset);
        Assert.assertEquals("myEntryName", result);

        name = "myEntryName\u0001";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        result = new String(buffer, 0, offset);
        Assert.assertEquals("myEntryName", result);
    }

    /**
     * Test for formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x00000012;
        byte[] buffer = new byte[TarConstants.MAXID + 1];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0000012", result);

        value = 0x12345678;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("01234567", result);

        value = 0x7fffffff;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0777777", result);
    }

    /**
     * Test for formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 0x00000012;
        byte[] buffer = new byte[TarConstants.MAXID + 3];

        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0000012 ", result);

        value = 0x12345678;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("01234567  ", result);

        value = 0x7fffffff;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0777777  ", result);

        value = 0x80000000;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        try {
            TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for formatLongOctalBytes method.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 0x00000012;
        byte[] buffer = new byte[TarConstants.MAXSIZE + 3];

        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0000012 ", result);

        value = 0x1234567890123456;
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0123456777623456", result);

        value = Long.MAX_VALUE;
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        try {
            TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test for formatLongOctalOrBinaryBytes method.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x00000012;
        byte[] buffer = new byte[TarConstants.MAXID + 1];

        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0000012", result);

        value = 0x12345678;
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("01234567", result);

        value = 0x1234567890123456;
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("0123456777623456", result);

        value = 0xffffffffffffffffL;
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        value = 0x123456789012345678L;
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        result = new String(buffer, 0, buffer.length);
        Assert.assertEquals("01234567776234567756", result);
    }

    /**
     * Test for computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd'};
        long checkSum = TarUtils.computeCheckSum(buffer);
        long computedCheckSum = 0;

        for (byte b : buffer) {
            computedCheckSum += TarUtils.BYTE_MASK & b;
        }

        Assert.assertEquals(checkSum, computedCheckSum);
    }
}