package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput_ReturnsExpectedResult() {
        byte[] buffer = {'1', '2', '3'};
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte_ThrowsIllegalArgumentException() {
        byte[] buffer = {'1', '2', 'g'};
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_TrailingCharacterMissing_ThrowsIllegalArgumentException() {
        byte[] buffer = {'1', '2'};
        int offset = 0;
        int length = 2;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidBytesAfterLeadingSpaces_ThrowsIllegalArgumentException() {
        byte[] buffer = {' ', ' ', '1', '2', '3', '4', '5'};
        int offset = 0;
        int length = 7;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName_ValidInput_ReturnsExpectedResult() {
        byte[] buffer = {'t', 'e', 's', 't', ' ', 'c', 'a', 's', 'e', 0, 'o', 't', 'h', 'e', 'r'};
        int offset = 0;
        int length = 10;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("test case", result);
    }

    @Test
    public void testParseName_TrailingNull_ReturnsExpectedResult() {
        byte[] buffer = {'t', 'e', 's', 't', 0};
        int offset = 0;
        int length = 5;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("test", result);
    }

    @Test
    public void testFormatNameBytes_ValidInput_ReturnsExpectedResult() {
        String name = "test case";
        byte[] buf = new byte[11];
        int offset = 0;
        int length = 11;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(11, result);
        assertArrayEquals(new byte[] {'t', 'e', 's', 't', ' ', 'c', 'a', 's', 'e', 0, 0}, buf);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer_ReturnsExpectedResult() {
        String name = "test case";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[] {'t', 'e', 's', 't', 0}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput_ReturnsExpectedResult() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        
        assertArrayEquals(new byte[] {'1', '7', '3', 0, '0'}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueTooLargeForBuffer_ThrowsIllegalArgumentException() {
        long value = 123;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_ValidInput_ReturnsExpectedResult() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        
        assertEquals(5, result);
        assertArrayEquals(new byte[] {'1', '7', '3', ' ', 0}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueTooLargeForBuffer_ThrowsIllegalArgumentException() {
        long value = 123;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;

        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_ValidInput_ReturnsExpectedResult() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        
        assertEquals(4, result);
        assertArrayEquals(new byte[] {'1', '7', '3', ' '}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_ValueTooLargeForBuffer_ThrowsIllegalArgumentException() {
        long value = 123;
        byte[] buffer = new byte[2];
        int offset = 0;
        int length = 2;

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput_ReturnsExpectedResult() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        
        assertEquals(5, result);
        assertArrayEquals(new byte[] {'1', '7', '3', 0, ' '}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_ValueTooLargeForBuffer_ThrowsIllegalArgumentException() {
        long value = 123;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;

        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum_ValidInput_ReturnsExpectedResult() {
        byte[] buf = {'1', '2', '3', '4', '5'};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(53, result);
    }

    @Test
    public void testComputeCheckSum_AllZeroBytes_ReturnsExpectedResult() {
        byte[] buf = {0, 0, 0, 0, 0, 0, 0, 0};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(0, result);
    }

    @Test
    public void testComputeCheckSum_MaximumByteValue_ReturnsExpectedResult() {
        byte[] buf = {127, 127, 127, 127, 127, 127, 127, 127};
        
        long result = TarUtils.computeCheckSum(buf);
        
        assertEquals(1016, result);
    }
}