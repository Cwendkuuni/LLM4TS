import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_WhenValidInput_ThenReturnLongValue() {
        byte[] buffer = new byte[]{ '1', '2', '3' };
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(83, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_WhenInvalidDigit_ThenThrowIllegalArgumentException() {
        byte[] buffer = new byte[]{ '1', 'a', '3' };
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseOctal_WhenTrailingNull_ThenStopParsing() {
        byte[] buffer = new byte[]{ '1', '2', '0' };
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(12, result);
    }

    @Test
    public void testParseOctal_WhenTrailingSpace_ThenStopParsing() {
        byte[] buffer = new byte[]{ '1', '2', '3', ' ' };
        int offset = 0;
        int length = 4;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(83, result);
    }

    @Test
    public void testParseName_WhenValidInput_ThenReturnString() {
        byte[] buffer = new byte[]{ 'a', 'b', 'c', 0 };
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("abc", result);
    }

    @Test
    public void testParseName_WhenTrailingNull_ThenStopParsing() {
        byte[] buffer = new byte[]{ 'a', 'b', 'c', 'd', 0 };
        int offset = 0;
        int length = 5;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes_WhenValidInput_ThenCopyCharsIntoBuffer() {
        String name = "test";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(new byte[]{ 't', 'e', 's', 't', 0 }, buf);
        assertEquals(5, result);
    }

    @Test
    public void testFormatNameBytes_WhenBufferLongerThanName_ThenFillBufferWithNULs() {
        String name = "test";
        byte[] buf = new byte[6];
        int offset = 0;
        int length = 6;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(new byte[]{ 't', 'e', 's', 't', 0, 0 }, buf);
        assertEquals(6, result);
    }

    @Test
    public void testFormatNameBytes_WhenNameLongerThanBuffer_ThenTruncateOutput() {
        String name = "test";
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertArrayEquals(new byte[]{ 't', 'e', 's' }, buf);
        assertEquals(3, result);
    }

    @Test
    public void testFormatUnsignedOctalString_WhenZeroValue_ThenFillBufferWithZero() {
        long value = 0;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = 3;

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        assertArrayEquals(new byte[]{ '0', '0', '0' }, buf);
    }

    @Test
    public void testFormatUnsignedOctalString_WhenNonZeroValue_ThenFillBufferWithOctalString() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);

        assertArrayEquals(new byte[]{ '0', '1', '7', '3', '0' }, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_WhenValueTooLarge_ThenThrowIllegalArgumentException() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
    }

    @Test
    public void testFormatOctalBytes_WhenValidInput_ThenWriteOctalNumberIntoBuffer() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertArrayEquals(new byte[]{ '0', '0', '1', '2', '3' }, buf);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_WhenValueTooLarge_ThenThrowIllegalArgumentException() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes_WhenValidInput_ThenWriteOctalNumberIntoBuffer() {
        long value = 123;
        byte[] buf = new byte[4];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertArrayEquals(new byte[]{ '0', '0', '1', '2' }, buf);
        assertEquals(4, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_WhenValueTooLarge_ThenThrowIllegalArgumentException() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_WhenValidInput_ThenWriteOctalNumberIntoBuffer() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertArrayEquals(new byte[]{ '0', '0', '1', '2', 0 }, buf);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_WhenValueTooLarge_ThenThrowIllegalArgumentException() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum_WhenValidInput_ThenReturnCheckSum() {
        byte[] buf = new byte[]{ 'a', 'b', 'c', 'd', 'e' };

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(495, result);
    }

}