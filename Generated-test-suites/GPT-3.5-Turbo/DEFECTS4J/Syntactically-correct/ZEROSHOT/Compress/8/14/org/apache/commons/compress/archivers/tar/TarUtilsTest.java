import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1
        byte[] buffer1 = {'1', '2', '3', '4', '\0'};
        int offset1 = 0;
        int length1 = 4;
        long result1 = TarUtils.parseOctal(buffer1, offset1, length1);
        assertEquals(1234, result1);

        // Test case 2
        byte[] buffer2 = {' ', ' ', '5', '6', '7', ' ', '\0'};
        int offset2 = 0;
        int length2 = 6;
        long result2 = TarUtils.parseOctal(buffer2, offset2, length2);
        assertEquals(567, result2);

        // Test case 3
        byte[] buffer3 = {'1', '2', 'A', '4', '\0'};
        int offset3 = 0;
        int length3 = 4;
        try {
            TarUtils.parseOctal(buffer3, offset3, length3);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid byte 65 at offset 2 in '12A4' len=4", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // Test case 1
        byte[] buffer1 = {'f', 'i', 'l', 'e', '1', '\0', 'e', 'x', 't'};
        int offset1 = 0;
        int length1 = 9;
        String result1 = TarUtils.parseName(buffer1, offset1, length1);
        assertEquals("file1", result1);

        // Test case 2
        byte[] buffer2 = {'m', 'y', ' ', 'f', 'i', 'l', 'e', '\0'};
        int offset2 = 0;
        int length2 = 7;
        String result2 = TarUtils.parseName(buffer2, offset2, length2);
        assertEquals("my file", result2);
    }

    @Test
    public void testFormatNameBytes() {
        // Test case 1
        String name1 = "file1";
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = 6;
        int result1 = TarUtils.formatNameBytes(name1, buf1, offset1, length1);
        assertEquals("file1\0\0\0", new String(buf1, 0, result1));

        // Test case 2
        String name2 = "my file";
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = 7;
        int result2 = TarUtils.formatNameBytes(name2, buf2, offset2, length2);
        assertEquals("my file", new String(buf2, 0, result2));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Test case 1
        long value1 = 1234;
        byte[] buffer1 = new byte[6];
        int offset1 = 0;
        int length1 = 6;
        TarUtils.formatUnsignedOctalString(value1, buffer1, offset1, length1);
        assertEquals("001234", new String(buffer1));

        // Test case 2
        long value2 = 567;
        byte[] buffer2 = new byte[6];
        int offset2 = 0;
        int length2 = 6;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        assertEquals("000567", new String(buffer2));

        // Test case 3
        long value3 = 8901;
        byte[] buffer3 = new byte[4];
        int offset3 = 0;
        int length3 = 4;
        try {
            TarUtils.formatUnsignedOctalString(value3, buffer3, offset3, length3);
        } catch (IllegalArgumentException e) {
            assertEquals("8901=21315 will not fit in octal number buffer of length 4", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // Test case 1
        long value1 = 1234;
        byte[] buf1 = new byte[8];
        int offset1 = 0;
        int length1 = 8;
        int result1 = TarUtils.formatOctalBytes(value1, buf1, offset1, length1);
        assertEquals("001234  ", new String(buf1, 0, result1));

        // Test case 2
        long value2 = 567;
        byte[] buf2 = new byte[7];
        int offset2 = 0;
        int length2 = 7;
        int result2 = TarUtils.formatOctalBytes(value2, buf2, offset2, length2);
        assertEquals("000567 ", new String(buf2, 0, result2));
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Test case 1
        long value1 = 1234;
        byte[] buf1 = new byte[8];
        int offset1 = 0;
        int length1 = 8;
        int result1 = TarUtils.formatLongOctalBytes(value1, buf1, offset1, length1);
        assertEquals("001234 ", new String(buf1, 0, result1));

        // Test case 2
        long value2 = 567;
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 8;
        int result2 = TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2);
        assertEquals("000567 ", new String(buf2, 0, result2));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Test case 1
        long value1 = 1234;
        byte[] buf1 = new byte[8];
        int offset1 = 0;
        int length1 = 8;
        int result1 = TarUtils.formatCheckSumOctalBytes(value1, buf1, offset1, length1);
        assertEquals("001234\0 ", new String(buf1, 0, result1));

        // Test case 2
        long value2 = 567;
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 8;
        int result2 = TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2);
        assertEquals("000567\0 ", new String(buf2, 0, result2));
    }

    @Test
    public void testComputeCheckSum() {
        // Test case 1
        byte[] buf1 = {'1', '2', '3', '4'};
        long result = TarUtils.computeCheckSum(buf1);
        assertEquals(448, result);

        // Test case 2
        byte[] buf2 = {'5', '6', '7'};
        result = TarUtils.computeCheckSum(buf2);
        assertEquals(218, result);
    }
}