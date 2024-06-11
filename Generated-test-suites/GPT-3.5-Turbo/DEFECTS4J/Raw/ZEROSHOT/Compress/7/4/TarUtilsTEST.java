import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1
        byte[] buffer1 = {49, 50, 51};
        int offset1 = 0;
        int length1 = 3;
        long expected1 = 123;
        long result1 = TarUtils.parseOctal(buffer1, offset1, length1);
        assertEquals(expected1, result1);

        // Test case 2
        byte[] buffer2 = {48, 55, 54};
        int offset2 = 0;
        int length2 = 3;
        long expected2 = 076;
        long result2 = TarUtils.parseOctal(buffer2, offset2, length2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testParseName() {
        // Test case 1
        byte[] buffer1 = {116, 101, 115, 116};
        int offset1 = 0;
        int length1 = 4;
        String expected1 = "test";
        String result1 = TarUtils.parseName(buffer1, offset1, length1);
        assertEquals(expected1, result1);

        // Test case 2
        byte[] buffer2 = {104, 101, 108, 108, 111};
        int offset2 = 0;
        int length2 = 5;
        String expected2 = "hello";
        String result2 = TarUtils.parseName(buffer2, offset2, length2);
        assertEquals(expected2, result2);
    }

    @Test
    public void testFormatNameBytes() {
        // Test case 1
        String name1 = "test";
        byte[] buf1 = new byte[5];
        int offset1 = 0;
        int length1 = 5;
        int expected1 = 5;
        int result1 = TarUtils.formatNameBytes(name1, buf1, offset1, length1);
        assertEquals(expected1, result1);
        assertArrayEquals(new byte[]{116, 101, 115, 116, 0}, buf1);

        // Test case 2
        String name2 = "hello";
        byte[] buf2 = new byte[7];
        int offset2 = 0;
        int length2 = 7;
        int expected2 = 7;
        int result2 = TarUtils.formatNameBytes(name2, buf2, offset2, length2);
        assertEquals(expected2, result2);
        assertArrayEquals(new byte[]{104, 101, 108, 108, 111, 0, 0}, buf2);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Test case 1
        long value1 = 123;
        byte[] buffer1 = new byte[5];
        int offset1 = 0;
        int length1 = 5;
        TarUtils.formatUnsignedOctalString(value1, buffer1, offset1, length1);
        assertArrayEquals(new byte[]{49, 50, 51, 0, 0}, buffer1);

        // Test case 2
        long value2 = 456;
        byte[] buffer2 = new byte[6];
        int offset2 = 0;
        int length2 = 6;
        TarUtils.formatUnsignedOctalString(value2, buffer2, offset2, length2);
        assertArrayEquals(new byte[]{55, 54, 0, 0, 0, 0}, buffer2);
    }

    @Test
    public void testFormatOctalBytes() {
        // Test case 1
        long value1 = 123;
        byte[] buf1 = new byte[6];
        int offset1 = 0;
        int length1 = 6;
        int expected1 = 6;
        int result1 = TarUtils.formatOctalBytes(value1, buf1, offset1, length1);
        assertEquals(expected1, result1);
        assertArrayEquals(new byte[]{49, 50, 51, 32, 0, 0}, buf1);

        // Test case 2
        long value2 = 456;
        byte[] buf2 = new byte[7];
        int offset2 = 0;
        int length2 = 7;
        int expected2 = 7;
        int result2 = TarUtils.formatOctalBytes(value2, buf2, offset2, length2);
        assertEquals(expected2, result2);
        assertArrayEquals(new byte[]{55, 54, 32, 0, 0, 0, 0}, buf2);
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Test case 1
        long value1 = 123;
        byte[] buf1 = new byte[5];
        int offset1 = 0;
        int length1 = 5;
        int expected1 = 5;
        int result1 = TarUtils.formatLongOctalBytes(value1, buf1, offset1, length1);
        assertEquals(expected1, result1);
        assertArrayEquals(new byte[]{49, 50, 51, 32, 0}, buf1);

        // Test case 2
        long value2 = 456;
        byte[] buf2 = new byte[6];
        int offset2 = 0;
        int length2 = 6;
        int expected2 = 6;
        int result2 = TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2);
        assertEquals(expected2, result2);
        assertArrayEquals(new byte[]{55, 54, 32, 0, 0, 0}, buf2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Test case 1
        long value1 = 123;
        byte[] buf1 = new byte[6];
        int offset1 = 0;
        int length1 = 6;
        int expected1 = 6;
        int result1 = TarUtils.formatCheckSumOctalBytes(value1, buf1, offset1, length1);
        assertEquals(expected1, result1);
        assertArrayEquals(new byte[]{49, 50, 51, 0, 32, 0}, buf1);

        // Test case 2
        long value2 = 456;
        byte[] buf2 = new byte[7];
        int offset2 = 0;
        int length2 = 7;
        int expected2 = 7;
        int result2 = TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2);
        assertEquals(expected2, result2);
        assertArrayEquals(new byte[]{55, 54, 0, 32, 0, 0, 0}, buf2);
    }

    @Test
    public void testComputeCheckSum() {
        // Test case 1
        byte[] buf1 = {49, 50, 51};
        long expected1 = 147;
        long result1 = TarUtils.computeCheckSum(buf1);
        assertEquals(expected1, result1);

        // Test case 2
        byte[] buf2 = {48, 55, 54};
        long expected2 = 151;
        long result2 = TarUtils.computeCheckSum(buf2);
        assertEquals(expected2, result2);
    }
}