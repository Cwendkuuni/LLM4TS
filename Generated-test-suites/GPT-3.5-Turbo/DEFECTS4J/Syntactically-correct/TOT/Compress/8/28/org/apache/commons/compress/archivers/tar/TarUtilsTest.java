import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctal_ReturnsParsedValue() {
        byte[] buffer = "   755\0".getBytes();
        long expected = 493;
        long actual = TarUtils.parseOctal(buffer, 0, 7);
        Assert.assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidCharacter_ThrowsIllegalArgumentException() {
        byte[] buffer = " 7a5\0".getBytes();
        TarUtils.parseOctal(buffer, 0, 6);
    }
    
    @Test
    public void testParseOctal_BufferContainsLeadingAndTrailingSpaces_ReturnsParsedValue() {
        byte[] buffer = "   755   \0".getBytes();
        long expected = 493;
        long actual = TarUtils.parseOctal(buffer, 0, 11);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParseOctal_BufferContainsAllNULs_ReturnsZero() {
        byte[] buffer = "\0\0\0\0\04\0\0\0\0\0\0\0".getBytes();
        long expected = 0;
        long actual = TarUtils.parseOctal(buffer, 0, 12);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParseName_ValidName_ReturnsParsedName() {
        byte[] buffer = "test.txt\0\0\0\0\0\0\0\0\0\0\0\0".getBytes();
        String expected = "test.txt";
        String actual = TarUtils.parseName(buffer, 0, 12);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParseName_BufferContainsLeadingAndTrailingNULs_ReturnsEmptyString() {
        byte[] buffer = "\0\0\0test.txt\0\0\0\0\0\0\0\0".getBytes();
        String expected = "";
        String actual = TarUtils.parseName(buffer, 0, 12);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testParseName_BufferContainsInvalidCharacters_ReturnsValidNameUntilInvalidCharacter() {
        byte[] buffer = "test.txtNUL\0\0\0\0\0\0\0\0\0".getBytes();
        String expected = "test.txt";
        String actual = TarUtils.parseName(buffer, 0, 13);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFormatNameBytes_NameFitsInBuffer_NameCopiedToBuffer() {
        String name = "test.txt";
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = "test.txt\0\0\0\0\0\0\0\0".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testFormatNameBytes_NameExceedsBufferLength_NameTruncatedInBuffer() {
        String name = "test.txt";
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        byte[] expected = "test.txt".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testFormatUnsignedOctalString_ValueIsZero_BufferFilledWithZeros() {
        long value = 0;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = "0000000\0".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testFormatUnsignedOctalString_ValueFitsInBuffer_BufferFilledWithOctalRepresentation() {
        long value = 493;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        byte[] expected = "0755".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueExceedsBufferLength_ThrowsIllegalArgumentException() {
        long value = 100000000000000L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 12;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }
    
    @Test
    public void testFormatOctalBytes_ValueFitsInBuffer_BufferContainsOctalValueWithTrailingSpaceAndNUL() {
        long value = 493;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        byte[] expected = "0755\0 \0".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testFormatLongOctalBytes_ValueFitsInBuffer_BufferContainsOctalValueWithTrailingSpace() {
        long value = 493;
        byte[] buffer = new byte[7];
        int offset = 0;
        int length = 7;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        byte[] expected = "0755 \0".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testFormatCheckSumOctalBytes_ValueFitsInBuffer_BufferContainsOctalValueWithTrailingNULAndSpace() {
        long value = 493;
        byte[] buffer = new byte[8];
        int offset = 0;
        int length = 8;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        byte[] expected = "0755\0 \0".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testComputeCheckSum_BufferContainsAllZeros_ReturnsZero() {
        byte[] buffer = new byte[8];
        long expected = 0;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeCheckSum_BufferContainsPositiveBytes_ReturnsSumOfPositiveBytes() {
        byte[] buffer = {1, 2, 3, 4, 5, 6, 7, 8};
        long expected = 36;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeCheckSum_BufferContainsNegativeBytes_ReturnsSumOfUnsignedBytes() {
        byte[] buffer = {(byte) -1, (byte) -2, (byte) -3, (byte) -4, (byte) -5, (byte) -6, (byte) -7, (byte) -8};
        long expected = 240;
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }

}