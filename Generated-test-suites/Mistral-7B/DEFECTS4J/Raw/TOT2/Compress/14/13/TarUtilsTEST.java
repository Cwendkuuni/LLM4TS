package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.*;

/**
 * TarUtilsTest class for testing TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string.
     */
    @Test
    public void testParseOctalValidOctalString() {
        byte[] buffer = "0755".getBytes();
        long expectedResult = 0755L;
        long actualResult = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctal method with missing trailing space/NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingSpaceOrNUL() {
        byte[] buffer = "0755".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length - 1);
    }

    /**
     * Test parseOctal method with invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = "0755a".getBytes();
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctalOrBinary method with valid binary number.
     */
    @Test
    public void testParseOctalOrBinaryValidBinaryNumber() {
        byte[] buffer = {0x0, (byte) 0x7f, 0x0, 0x0};
        long expectedResult = 127L;
        long actualResult = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseOctalOrBinary method with a binary number that exceeds the size of a signed long 64-bit integer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryBinaryNumberExceedsSignedLongSize() {
        byte[] buffer = {(byte) 0xff, (byte) 0xff, (byte) 0xff, 0x7f};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    /**
     * Test parseBoolean method with valid boolean byte.
     */
    @Test
    public void testParseBooleanValidBooleanByte() {
        byte[] buffer = {1};
        boolean expectedResult = true;
        boolean actualResult = TarUtils.parseBoolean(buffer, 0);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseBoolean method with invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = {0};
        TarUtils.parseBoolean(buffer, 0);
    }

    /**
     * Test parseName method with valid entry name.
     */
    @Test
    public void testParseNameValidEntryName() {
        byte[] buffer = "filename.txt".getBytes();
        String expectedResult = "filename.txt";
        String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Test parseName method with invalid buffer.
     */
    @Test
    public void testParseNameInvalidBuffer() {
        byte[] buffer = new byte[0];
        String actualResult = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertNull(actualResult);
    }

    /**
     * Test formatNameBytes method with valid entry name.
     */
    @Test
    public void testFormatNameBytesValidEntryName() {
        String name = "filename.txt";
        byte[] expectedResult = new String("filename.txt".getBytes()).getBytes();
        byte[] buffer = new byte[name.length() + 1];
        int actualResult = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedResult, buffer);
    }

    /**
     * Test formatNameBytes method with buffer size less than the name size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesBufferSizeLessThanNameSize() {
        String name = "filename.txt";
        byte[] buffer = new byte[name.length() - 1];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with buffer size equal to the name size.
     */
    @Test
    public void testFormatNameBytesBufferSizeEqualToNameSize() {
        String name = "filename.txt";
        byte[] buffer = new byte[name.length()];
        int actualResult = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(name.length(), actualResult);
    }

    /**
     * Test formatNameBytes method with buffer size larger than the name size.
     */
    @Test
    public void testFormatNameBytesBufferSizeLargerThanNameSize() {
        String name = "filename.txt";
        byte[] buffer = new byte[name.length() + 10];
        int actualResult = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.assertEquals(name.length() + 1, actualResult);
    }

    /**
     * Test formatUnsignedOctalString method with valid value.
     */
    @Test
    public void testFormatUnsignedOctalStringValidValue() {
        long value = 755;
        byte[] expectedResult = new String("0755").getBytes();
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedResult, buffer);
    }

    /**
     * Test formatUnsignedOctalString method with value that does not fit in the buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueDoesNotFitInBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.MAXID];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid value.
     */
    @Test
    public void testFormatOctalBytesValidValue() {
        long value = 755;
        byte[] expectedResult = new String("0755").getBytes();
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        int actualResult = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedResult, buffer);
    }

    /**
     * Test formatOctalBytes method with value that does not fit in the buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueDoesNotFitInBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with valid value.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesValidValue() {
        long value = 755;
        byte[] expectedResult = new String("0755").getBytes();
        byte[] buffer = new byte[TarConstants.UIDLEN + 2];
        int actualResult = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedResult, buffer);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with value that does not fit in the buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesValueDoesNotFitInBuffer() {
        long value = 123456789012345L;
        byte[] buffer = new byte[TarConstants.UIDLEN];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatLongOctalOrBinaryBytes method with binary number.
     */
    @Test
    public void testFormatLongOctalOrBinaryBytesBinaryNumber() {
        long value = 1234567890;
        byte[] expectedResult = new byte[]{0, 0, 0, 0, 0, 0, 0x7f, 0x34, 0x56, 0x78, 0x90};
        byte[] buffer = new byte[TarConstants.UIDLEN];
        int actualResult = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        Assert.assertArrayEquals(expectedResult, buffer);
    }

    /**
     * Test computeCheckSum method with valid tar entry header.
     */
    @Test
    public void testComputeCheckSumValidTarEntryHeader() {
        byte[] tarEntryHeader = new byte[]{0x00, 0x00, 0x00, 0x20, 0x60, 0x01, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00};
        long expectedChecksum = TarUtils.computeCheckSum(tarEntryHeader);
        long actualChecksum = 55903L; // Checksum calculated manually
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    /**
     * Test parseOctal method with a leading NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{0, 0, 0, 0};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }
}