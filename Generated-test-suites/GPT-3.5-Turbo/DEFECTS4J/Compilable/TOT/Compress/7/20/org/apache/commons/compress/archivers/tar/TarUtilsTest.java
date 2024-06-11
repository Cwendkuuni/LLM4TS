package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_WithValidInput_ShouldReturnLongValue() {
        byte[] buffer = {(byte) '1', (byte) '2', (byte) '3', (byte) '4'};
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(1234, result);
    }

    @Test
    public void testParseOctal_WithLeadingSpaces_ShouldIgnoreSpacesAndReturnLongValue() {
        byte[] buffer = {(byte) ' ', (byte) ' ', (byte) '1', (byte) '0'};
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(10, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_WithNonOctalInput_ShouldThrowIllegalArgumentException() {
        byte[] buffer = {(byte) '1', (byte) '2', (byte) 'A', (byte) '4'};
        int offset = 0;
        int length = 4;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_WithValidInput_ShouldReturnString() {
        byte[] buffer = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) 0};
        int offset = 0;
        int length = 5;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("test", result);
    }

    @Test
    public void testParseName_WithTrailingSpaces_ShouldIgnoreSpacesAndReturnString() {
        byte[] buffer = {(byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) ' ', (byte) ' ', (byte) 0};
        int offset = 0;
        int length = 7;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_WithNameShorterThanBuffer_ShouldCopyNameAndFillRemainingWithNUL() {
        String name = "test";
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals("test\0\0\0\0\0", new String(buffer));
        assertEquals(8, result);
    }

    @Test
    public void testFormatNameBytes_WithNameLongerThanBuffer_ShouldTruncateOutput() {
        String name = "testtesttest";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals("testtestt\0", new String(buffer));
        assertEquals(10, result);
    }

    @Test
    public void testFormatUnsignedOctalString_WithValidValue_ShouldFormatValueWithLeadingZeros() {
        long value = 1234;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("01234", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_WithInvalidValue_ShouldThrowIllegalArgumentException() {
        long value = 99999999999L;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_ShouldWriteOctalValueIntoBufferWithTrailingSpaceAndNUL() {
        long value = 1234;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertEquals("00001234 ", new String(buffer));
        assertEquals(8, result);
    }

    @Test
    public void testFormatLongOctalBytes_ShouldWriteOctalValueIntoBufferWithTrailingSpace() {
        long value = 1234;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertEquals("00001234", new String(buffer));
        assertEquals(8, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ShouldWriteOctalValueIntoBufferWithTrailingNULAndSpace() {
        long value = 1234;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertEquals("00001234\0 ", new String(buffer));
        assertEquals(8, result);
    }

    @Test
    public void testComputeCheckSum_WithValidBuffer_ShouldReturnSumOfBytes() {
        byte[] buffer = {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04};
        long expectedSum = 10;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expectedSum, result);
    }

    @Test
    public void testComputeCheckSum_WithEmptyBuffer_ShouldReturnZero() {
        byte[] buffer = new byte[0];

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(0, result);
    }

}