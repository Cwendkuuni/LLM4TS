import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = '3';
        long result = TarUtils.parseOctal(buffer, 0, 3);
        assertEquals(83, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidDigit() {
        byte[] buffer = new byte[10];
        buffer[0] = '1';
        buffer[1] = '2';
        buffer[2] = 'x';
        TarUtils.parseOctal(buffer, 0, 3);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 'h';
        buffer[1] = 'e';
        buffer[2] = 'l';
        buffer[3] = 'l';
        buffer[4] = 'o';
        String result = TarUtils.parseName(buffer, 0, 5);
        assertEquals("hello", result);
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[10];
        String name = "hello";
        int offset = 0;
        int length = 6;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(6, result);
        assertEquals('h', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
        assertEquals(0, buffer[5]);
        assertEquals(0, buffer[6]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 83;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 4;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(0, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringBufferTooSmall() {
        long value = 1234;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 3;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(4, result);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(4, result);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(' ', buffer[3]);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 83;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(4, result);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(' ', buffer[4]);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 'h';
        buf[1] = 'e';
        buf[2] = 'l';
        buf[3] = 'l';
        buf[4] = 'o';
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(532, result);
    }
}