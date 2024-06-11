import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1: buffer is empty
        byte[] buffer1 = new byte[0];
        long result1 = TarUtils.parseOctal(buffer1, 0, 0);
        assertEquals(0, result1);

        // Test case 2: buffer contains valid octal digits
        byte[] buffer2 = "123456".getBytes();
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(34232, result2);

        // Test case 3: buffer contains non-octal digits
        byte[] buffer3 = "123xyz".getBytes();
        try {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 3 in '123xyz'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // Test case 1: buffer is empty
        byte[] buffer1 = new byte[0];
        String result1 = TarUtils.parseName(buffer1, 0, 0);
        assertEquals("", result1);

        // Test case 2: buffer contains valid name characters
        byte[] buffer2 = "filename".getBytes();
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertEquals("filename", result2);

        // Test case 3: buffer contains null character
        byte[] buffer3 = "filename\0".getBytes();
        String result3 = TarUtils.parseName(buffer3, 0, buffer3.length);
        assertEquals("filename", result3);
    }

    @Test
    public void testFormatNameBytes() {
        // Test case 1: name fits within buffer length
        String name1 = "filename";
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = name1.length();
        int result1 = TarUtils.formatNameBytes(name1, buf1, offset1, length1);
        assertEquals(10, result1);
        assertEquals("filename\0\0", new String(buf1));

        // Test case 2: name is longer than buffer length
        String name2 = "longfilename";
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        int result2 = TarUtils.formatNameBytes(name2, buf2, offset2, length2);
        assertEquals(10, result2);
        assertEquals("longfilena", new String(buf2));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Test case 1: value is zero
        long value1 = 0;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = buf1.length;
        TarUtils.formatUnsignedOctalString(value1, buf1, offset1, length1);
        assertEquals("0000000000", new String(buf1));

        // Test case 2: value is positive
        long value2 = 12345;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        TarUtils.formatUnsignedOctalString(value2, buf2, offset2, length2);
        assertEquals("0000003031", new String(buf2));
    }

    @Test
    public void testFormatOctalBytes() {
        // Test case 1: value is zero
        long value1 = 0;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = buf1.length;
        int result1 = TarUtils.formatOctalBytes(value1, buf1, offset1, length1);
        assertEquals(10, result1);
        assertEquals("0000000000 ", new String(buf1));

        // Test case 2: value is positive
        long value2 = 12345;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        int result2 = TarUtils.formatOctalBytes(value2, buf2, offset2, length2);
        assertEquals(10, result2);
        assertEquals("0000003031 ", new String(buf2));
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Test case 1: value is zero
        long value1 = 0;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = buf1.length;
        int result1 = TarUtils.formatLongOctalBytes(value1, buf1, offset1, length1);
        assertEquals(10, result1);
        assertEquals("0000000000 ", new String(buf1));

        // Test case 2: value is positive
        long value2 = 12345;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        int result2 = TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2);
        assertEquals(10, result2);
        assertEquals("0000003031 ", new String(buf2));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Test case 1: value is zero
        long value1 = 0;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = buf1.length;
        int result1 = TarUtils.formatCheckSumOctalBytes(value1, buf1, offset1, length1);
        assertEquals(10, result1);
        assertEquals("0000000000 ", new String(buf1));

        // Test case 2: value is positive
        long value2 = 12345;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = buf2.length;
        int result2 = TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2);
        assertEquals(10, result2);
        assertEquals("0000003031 ", new String(buf2));
    }

    @Test
    public void testComputeCheckSum() {
        // Test case 1: buffer is empty
        byte[] buf1 = new byte[0];
        long result1 = TarUtils.computeCheckSum(buf1);
        assertEquals(0, result1);

        // Test case 2: buffer contains characters
        byte[] buf2 = "test".getBytes();
        long result2 = TarUtils.computeCheckSum(buf2);
        assertEquals(448, result2);
    }
}