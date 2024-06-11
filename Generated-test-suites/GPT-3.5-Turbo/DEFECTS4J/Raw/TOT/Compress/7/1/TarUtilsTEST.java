import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = "123".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(123, result);

        buffer = "01234567".getBytes();
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(342391, result);

        buffer = "invalid".getBytes();
        assertThrows(IllegalArgumentException.class,
                () -> TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "hello".getBytes();
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello", result);

        buffer = "hello\u0000world".getBytes();
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(4, result);
        assertEquals("test\u0000\u0000\u0000\u0000\u0000", new String(buf));

        name = "longername";
        buf = new byte[10];
        result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(10, result);
        assertEquals("longername", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertEquals("0000000123", new String(buf));

        value = 342391;
        buf = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
        assertEquals("0132236227", new String(buf));

        value = 1234567890123456789L;
        buf = new byte[10];
        assertThrows(IllegalArgumentException.class,
                () -> TarUtils.formatUnsignedOctalString(value, buf, offset, length));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("0000000123 ", new String(buf));

        value = 342391;
        buf = new byte[10];
        result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("0132236227 ", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("00000000123", new String(buf));

        value = 342391;
        buf = new byte[10];
        result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("00000033622", new String(buf));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("0000000123 ", new String(buf));

        value = 342391;
        buf = new byte[10];
        result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(10, result);
        assertEquals("0132236227 ", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = "hello".getBytes();
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(532, result);

        buf = new byte[0];
        result = TarUtils.computeCheckSum(buf);
        assertEquals(0, result);
    }

}