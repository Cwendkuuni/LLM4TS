import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validInput_returnsLongValue() {
        byte[] buffer = " 123".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 83L;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidInput_throwsIllegalArgumentException() {
        byte[] buffer = " 0A1".getBytes();
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseOctal_bufferWithOnlyNULs_returnsZero() {
        byte[] buffer = "\0\0\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        long expected = 0L;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_validInput_returnsEntryName() {
        byte[] buffer = "MyEntryName\0".getBytes();
        int offset = 0;
        int length = buffer.length;
        String expected = "MyEntryName";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testParseName_inputWithTrailingNull_returnsEntryName() {
        byte[] buffer = "MyEntryName\0Extra".getBytes();
        int offset = 0;
        int length = buffer.length;
        String expected = "MyEntryName";

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_shortNameAndBufferLongerThanName_returnsBufferWithTrailingNULs() {
        String name = "Name";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "Name\0\0\0\0\0".getBytes();

        TarUtils.formatNameBytes(name, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatNameBytes_longNameAndBufferShorterThanName_returnsTruncatedBuffer() {
        String name = "VeryLongName";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = "VeryL".getBytes();

        TarUtils.formatNameBytes(name, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString_positiveNumber_returnsOctalString() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = " 0000011640".getBytes();

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_negativeNumber_throwsIllegalArgumentException() {
        long value = -123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes_positiveNumber_returnsBufferWithTrailingSpaceAndNUL() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = " 000011640\0 ".getBytes();

        TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_negativeNumber_throwsIllegalArgumentException() {
        long value = -123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_positiveNumber_returnsBufferWithTrailingSpace() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = " 000011640 ".getBytes();

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_negativeNumber_throwsIllegalArgumentException() {
        long value = -123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_positiveNumber_returnsBufferWithTrailingNULAndSpace() {
        long value = 123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;
        byte[] expected = " 000011640\0 ".getBytes();

        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertArrayEquals(expected, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_negativeNumber_throwsIllegalArgumentException() {
        long value = -123456L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum_validInput_returnsChecksum() {
        byte[] buffer = "Test".getBytes();
        long expected = 460L;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSum_bufferWithAllZeros_returnsZero() {
        byte[] buffer = new byte[10];
        long expected = 0L;

        long result = TarUtils.computeCheckSum(buffer);

        assertEquals(expected, result);
    }

}