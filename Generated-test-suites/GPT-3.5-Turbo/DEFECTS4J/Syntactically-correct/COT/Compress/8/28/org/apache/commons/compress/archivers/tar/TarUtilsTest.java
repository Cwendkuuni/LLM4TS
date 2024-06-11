import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123".getBytes();
        int offset = 0;
        int length = buffer.length;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = "12Z".getBytes();
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = "HelloWorld\0".getBytes();
        int offset = 0;
        int length = 10;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("HelloWorld", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "HelloWorld";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(offset + length, result);
        assertEquals("HelloWorld", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals("00123", new String(buffer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringBufferOverflow() {
        long value = 123456789;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatOctalBytes(value, buf, offset, length);

        assertEquals(offset + length, result);
        assertEquals("00000123 ", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);

        assertEquals(offset + length, result);
        assertEquals("00000123 ", new String(buf));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[6];
        int offset = 0;
        int length = buf.length;

        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);

        assertEquals(offset + length, result);
        assertEquals("00000123\u0000 ", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(45, result);
    }
}