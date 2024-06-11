import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Typical Use Case
        byte[] buffer1 = "1234\0".getBytes();
        long result1 = TarUtils.parseOctal(buffer1, 0, 5);
        assertEquals(1234L, result1);

        // Edge Case: Empty buffer
        byte[] buffer2 = new byte[0];
        long result2 = TarUtils.parseOctal(buffer2, 0, 0);
        assertEquals(0L, result2);

        // Error Scenario: Non-octal input
        boolean thrown = false;
        try {
            byte[] buffer3 = "abcd\0".getBytes();
            TarUtils.parseOctal(buffer3, 0, 5);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testParseName() {
        // Typical Use Case
        byte[] buffer1 = "filename\0".getBytes();
        String result1 = TarUtils.parseName(buffer1, 0, 9);
        assertEquals("filename", result1);

        // Edge Case: Empty buffer
        byte[] buffer2 = new byte[0];
        String result2 = TarUtils.parseName(buffer2, 0, 0);
        assertEquals("", result2);
    }

    @Test
    public void testFormatNameBytes() {
        // Typical Use Case
        String name1 = "filename";
        byte[] buf1 = new byte[name1.length()];
        TarUtils.formatNameBytes(name1, buf1, 0, 8);
        assertEquals("filename", new String(buf1));

        // Edge Case: Empty name
        String name2 = "";
        byte[] buf2 = new byte[8];
        TarUtils.formatNameBytes(name2, buf2, 0, 8);
        assertEquals("\0\0\0\0\0\0\0\0", new String(buf2));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Typical Use Case
        long value1 = 1234L;
        byte[] buf1 = new byte[5];
        TarUtils.formatUnsignedOctalString(value1, buf1, 0, 5);
        assertEquals("1234\0", new String(buf1));

        // Edge Case: Zero value
        long value2 = 0L;
        byte[] buf2 = new byte[5];
        TarUtils.formatUnsignedOctalString(value2, buf2, 0, 5);
        assertEquals("0\0\0\0\0", new String(buf2));

        // Error Scenario: Value too large for buffer
        boolean thrown = false;
        try {
            long value3 = 1234567890L;
            byte[] buf3 = new byte[8];
            TarUtils.formatUnsignedOctalString(value3, buf3, 0, 8);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testFormatOctalBytes() {
        // Typical Use Case
        long value1 = 1234L;
        byte[] buf1 = new byte[8];
        int offset1 = TarUtils.formatOctalBytes(value1, buf1, 0, 6);
        assertEquals("001234 \0", new String(buf1));
        assertEquals(8, offset1);

        // Edge Case: Zero value
        long value2 = 0L;
        byte[] buf2 = new byte[8];
        int offset2 = TarUtils.formatOctalBytes(value2, buf2, 0, 6);
        assertEquals("000000 \0", new String(buf2));
        assertEquals(8, offset2);
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Typical Use Case
        long value1 = 1234L;
        byte[] buf1 = new byte[8];
        int offset1 = TarUtils.formatLongOctalBytes(value1, buf1, 0, 7);
        assertEquals("001234 \0", new String(buf1));
        assertEquals(8, offset1);

        // Edge Case: Zero value
        long value2 = 0L;
        byte[] buf2 = new byte[8];
        int offset2 = TarUtils.formatLongOctalBytes(value2, buf2, 0, 7);
        assertEquals("000000 \0", new String(buf2));
        assertEquals(8, offset2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Typical Use Case
        long value1 = 1234L;
        byte[] buf1 = new byte[8];
        int offset1 = TarUtils.formatCheckSumOctalBytes(value1, buf1, 0, 7);
        assertEquals("001234\0 ", new String(buf1));
        assertEquals(8, offset1);

        // Edge Case: Zero value
        long value2 = 0L;
        byte[] buf2 = new byte[8];
        int offset2 = TarUtils.formatCheckSumOctalBytes(value2, buf2, 0, 7);
        assertEquals("000000\0 ", new String(buf2));
        assertEquals(8, offset2);
    }

    @Test
    public void testComputeCheckSum() {
        // Typical Use Case
        byte[] buf1 = "1234".getBytes();
        long result1 = TarUtils.computeCheckSum(buf1);
        assertEquals(4, result1);

        // Edge Case: Empty buffer
        byte[] buf2 = new byte[0];
        long result2 = TarUtils.computeCheckSum(buf2);
        assertEquals(0, result2);
    }
}