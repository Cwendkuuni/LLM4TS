import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_typicalCase() {
        byte[] buffer = {49, 50, 51, 0, 0, 0}; // "123\0\0\0"
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_bufferWithLeadingSpaces() {
        byte[] buffer = {32, 32, 32, 49, 50, 51, 0, 0, 0}; // "   123\0\0\0"
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidOctalDigit() {
        byte[] buffer = {49, 50, 56, 0, 0, 0}; // "128\0\0\0"
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_typicalCase() {
        byte[] buffer = {97, 98, 99, 0, 0, 0}; // "abc\0\0\0"
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);
    }

    @Test
    public void testParseName_bufferWithNullBytes() {
        byte[] buffer = {97, 98, 99, 0, 100, 101}; // "abc\0de"
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes_typicalCase() {
        String name = "abc";
        byte[] buffer = new byte[6];
        int result = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        assertEquals(3, result);
        assertEquals((byte)97, buffer[0]);
        assertEquals((byte)98, buffer[1]);
        assertEquals((byte)99, buffer[2]);
        assertEquals((byte)0, buffer[3]);
        assertEquals((byte)0, buffer[4]);
        assertEquals((byte)0, buffer[5]);
    }

    @Test
    public void testFormatNameBytes_bufferLongerThanName() {
        String name = "abc";
        byte[] buffer = new byte[6];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals((byte)97, buffer[0]);
        assertEquals((byte)98, buffer[1]);
        assertEquals((byte)99, buffer[2]);
        assertEquals((byte)0, buffer[3]);
        assertEquals((byte)0, buffer[4]);
        assertEquals((byte)0, buffer[5]);
    }

    @Test
    public void testFormatNameBytes_nameLongerThanBuffer() {
        String name = "abcdefghi";
        byte[] buffer = new byte[6];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(6, result);
        assertEquals((byte)97, buffer[0]);
        assertEquals((byte)98, buffer[1]);
        assertEquals((byte)99, buffer[2]);
        assertEquals((byte)100, buffer[3]);
        assertEquals((byte)101, buffer[4]);
        assertEquals((byte)102, buffer[5]);
    }

    @Test
    public void testFormatUnsignedOctalString_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals((byte)51, buffer[0]);
        assertEquals((byte)50, buffer[1]);
        assertEquals((byte)49, buffer[2]);
    }

    @Test
    public void testFormatUnsignedOctalString_valueEqualsZero() {
        long value = 0;
        byte[] buffer = new byte[3];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals((byte)48, buffer[0]);
        assertEquals((byte)48, buffer[1]);
        assertEquals((byte)48, buffer[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_bufferTooSmall() {
        long value = 123;
        byte[] buffer = new byte[2];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[4];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(4, result);
        assertEquals((byte)51, buffer[0]);
        assertEquals((byte)50, buffer[1]);
        assertEquals((byte)49, buffer[2]);
        assertEquals((byte)32, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_bufferTooSmall() {
        long value = 123;
        byte[] buffer = new byte[3];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[3];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(3, result);
        assertEquals((byte)51, buffer[0]);
        assertEquals((byte)50, buffer[1]);
        assertEquals((byte)32, buffer[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_bufferTooSmall() {
        long value = 123;
        byte[] buffer = new byte[2];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[4];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(4, result);
        assertEquals((byte)51, buffer[0]);
        assertEquals((byte)50, buffer[1]);
        assertEquals((byte)32, buffer[2]);
        assertEquals((byte)0, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_bufferTooSmall() {
        long value = 123;
        byte[] buffer = new byte[3];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum_typicalCase() {
        byte[] buf = new byte[5];
        buf[0] = (byte)97; // 'a'
        buf[1] = (byte)98; // 'b'
        buf[2] = (byte)99; // 'c'
        buf[3] = (byte)0; // '\0'
        buf[4] = (byte)100; // 'd'
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(295, sum);
    }

    @Test
    public void testComputeCheckSum_bufferIsEmpty() {
        byte[] buf = new byte[0];
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(0, sum);
    }

}