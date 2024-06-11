import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '};
        long expected = 01234567L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', 'a', 'b', ' '};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', '.', 'm', 'p', ' ', 'f', 'i', 'l', 'e', 's', 'e', 0};
        String expected = "tar.amplestuffs.";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.amplestuffs.";
        byte[] buf = new byte[32];
        int offset = 0;
        int length = TarUtils.formatNameBytes(name, buf, offset, buf.length);
        String actual = new String(buf, 0, length);
        assertEquals(name, actual);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = TarUtils.formatUnsignedOctalString(value, buffer, offset, buffer.length);
        String expected = "01234567";
        String actual = new String(buffer, 0, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 1234567;
        byte[] buf = new byte[12];
        int offset = 0;
        int length = TarUtils.formatOctalBytes(value, buf, offset, buf.length);
        String expected = "01234567 \0";
        String actual = new String(buf, 0, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 12345678912345L;
        byte[] buf = new byte[14];
        int offset = 0;
        int length = TarUtils.formatCheckSumOctalBytes(value, buf, offset, buf.length);
        String expected = "0123456777665544 \0";
        String actual = new String(buf, 0, length);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '.', 'a', '.', 'm', 'p', ' ', 'f', 'i', 'l', 'e', 's', 'e', 0};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseOctal_EmptyBuffer() {
        byte[] buffer = new byte[0];
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseOctal_NullBuffer() {
        byte[] buffer = null;
        try {
            TarUtils.parseOctal(buffer, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testParseName_EmptyBuffer() {
        byte[] buffer = new byte[0];
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", actual);
    }

    @Test
    public void testParseName_NullBuffer() {
        byte[] buffer = null;
        try {
            TarUtils.parseName(buffer, 0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes_NullName() {
        String name = null;
        byte[] buf = new byte[10];
        int offset = 0;
        int length = TarUtils.formatNameBytes(name, buf, offset, buf.length);
        assertEquals(0, length);
    }

    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "This is a long name that will be truncated";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = TarUtils.formatNameBytes(name, buf, offset, buf.length);
        String actual = new String(buf, 0, length);
        String expected = "This is a long name that wil";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString_ValueTooLargeForBuffer() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = TarUtils.formatUnsignedOctalString(value, buffer, offset, buffer.length);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFormatOctalBytes_ValueTooLargeForBuffer() {
        long value = Long.MAX_VALUE + 1;
        byte[] buf = new byte[12];
        int offset = 0;
        int length = TarUtils.formatOctalBytes(value, buf, offset, buf.length);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFormatCheckSumOctalBytes_ValueTooLargeForBuffer() {
        long value = Long.MAX_VALUE + 1;
        byte[] buf = new byte[14];
        int offset = 0;
        int length = TarUtils.formatCheckSumOctalBytes(value, buf, offset, buf.length);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testComputeCheckSum_NullBuffer() {
        byte[] buffer = null;
        try {
            TarUtils.computeCheckSum(buffer);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Expected
        }
    }
}