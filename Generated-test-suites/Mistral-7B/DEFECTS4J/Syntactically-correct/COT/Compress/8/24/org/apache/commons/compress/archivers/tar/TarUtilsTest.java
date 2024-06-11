import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '0', '0'};
        long expected = 0175400L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', 'x', '0'}; // 'x' is not a valid byte for octal representation
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalTrailingZeroes() {
        byte[] buffer = new byte[]{'0', '7', '5', '4'};
        long expected = 01754L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseOctalEmpty() {
        byte[] buffer = new byte[0];
        long expected = 0L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 0x41, (byte) 0x72, (byte) 0x65, (byte) 0x67, (byte) 0x6F, (byte) 0x72, (byte) 0x6B, (byte) 0x00};
        String expected = "Apple";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseNameTrailingNull() {
        byte[] buffer = new byte[]{(byte) 0x41, (byte) 0x72, (byte) 0x65, (byte) 0x67, (byte) 0x6F, (byte) 0x72, (byte) 0x6B, 0x00};
        String expected = "Apple";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseNameEmpty() {
        byte[] buffer = new byte[0];
        String expected = "";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "MyName";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        int expected = 9;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, actual);
        String result = new String(buffer, 0, actual);
        assertEquals(name, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesLengthTooShort() {
        String name = "MyName";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = name.length();
        TarUtils.formatNameBytes(name, buffer, offset, length);
    }

    @Test
    public void testFormatNameBytesNameLongerThanBuffer() {
        String name = "MyNameIsLongerThanTheBuffer";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 9;
        int expected = 9;
        int actual = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(expected, actual);
        String result = new String(buffer, 0, actual);
        assertEquals(name.substring(0, 9), result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0x12345678L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "012451067014";
        String actual = new String(buffer, 0, length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueTooLarge() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0x12345678L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 9;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        String expected = "012451067014 ";
        String actual = new String(buffer, 0, length+2); // Add space and trailing null
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueTooLarge() {
        long value = 0x123456789012345L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 9;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x43, (byte) 0x61, (byte) 0x72, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x61, (byte) 0x75, (byte) 0x61, (byte) 0x74, (byte) 0x75, (byte) 0x72, (byte) 0x69, (byte) 0x6f, (byte) 0x6e};
        long expected = 0x54494d4c4c494f4e494f4e;
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0x54494d4c4c494f4e494f4eL;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 10;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        String expected = "10201343212110253112352105563365";
        String actual = new String(buffer, 0, length*2+2); // Add space and trailing null
        assertEquals(expected, actual);
    }
}