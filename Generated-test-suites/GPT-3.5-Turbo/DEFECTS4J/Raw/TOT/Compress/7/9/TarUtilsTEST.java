import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // typical use case
        byte[] buffer1 = {'1', '2', '3'};
        assertEquals(123, TarUtils.parseOctal(buffer1, 0, buffer1.length));

        // edge cases
        byte[] buffer2 = {'0', '0', '0'};
        assertEquals(0, TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = {'7', '7', '7'};
        assertEquals(511, TarUtils.parseOctal(buffer3, 0, buffer3.length));

        byte[] buffer4 = {' ', '1', '0'};
        assertEquals(10, TarUtils.parseOctal(buffer4, 0, buffer4.length));

        // error scenario
        byte[] buffer5 = {'1', 'a', '3'};
        try {
            TarUtils.parseOctal(buffer5, 0, buffer5.length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 1 in '1a3'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // typical use case
        byte[] buffer1 = {'h', 'e', 'l', 'l', 'o', 0};
        assertEquals("hello", TarUtils.parseName(buffer1, 0, buffer1.length));

        // edge case
        byte[] buffer2 = {0};
        assertEquals("", TarUtils.parseName(buffer2, 0, buffer2.length));

        // error scenario
        byte[] buffer3 = {'h', (byte) 0xFF, 'l', 'l', 'o'};
        assertEquals("h", TarUtils.parseName(buffer3, 0, buffer3.length));
    }

    @Test
    public void testFormatNameBytes() {
        // typical use case
        String name1 = "hello";
        byte[] buffer1 = new byte[5];
        TarUtils.formatNameBytes(name1, buffer1, 0, buffer1.length);
        assertArrayEquals(new byte[]{'h', 'e', 'l', 'l', 'o'}, buffer1);

        // edge case
        String name2 = "";
        byte[] buffer2 = new byte[10];
        TarUtils.formatNameBytes(name2, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, buffer2);

        // error scenario
        String name3 = "hello";
        byte[] buffer3 = new byte[3];
        try {
            TarUtils.formatNameBytes(name3, buffer3, 0, buffer3.length);
            fail("Expected ArrayIndexOutOfBoundsException to be thrown");
        } catch (ArrayIndexOutOfBoundsException e) {
            assertEquals("3", e.getMessage());
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // typical use case
        byte[] buffer1 = new byte[5];
        TarUtils.formatUnsignedOctalString(123, buffer1, 0, buffer1.length);
        assertArrayEquals(new byte[]{0, 0, 0, '1', '2'}, buffer1);

        // edge case
        byte[] buffer2 = new byte[5];
        TarUtils.formatUnsignedOctalString(0, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{0, 0, 0, 0, '0'}, buffer2);

        // error scenario
        byte[] buffer3 = new byte[3];
        try {
            TarUtils.formatUnsignedOctalString(256, buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("256=400 will not fit in octal number buffer of length 3", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // typical use case
        long value1 = 123;
        byte[] buffer1 = new byte[5];
        TarUtils.formatOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', '2'}, buffer1);

        // edge case
        long value2 = 0;
        byte[] buffer2 = new byte[5];
        TarUtils.formatOctalBytes(value2, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '0', ' '}, buffer2);

        // error scenario
        long value3 = 256;
        byte[] buffer3 = new byte[3];
        try {
            TarUtils.formatOctalBytes(value3, buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("256=400 will not fit in octal number buffer of length 3", e.getMessage());
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        // typical use case
        long value1 = 123;
        byte[] buffer1 = new byte[5];
        TarUtils.formatLongOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', ' '}, buffer1);

        // edge case
        long value2 = 0;
        byte[] buffer2 = new byte[5];
        TarUtils.formatLongOctalBytes(value2, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '0', ' '}, buffer2);

        // error scenario
        long value3 = 256;
        byte[] buffer3 = new byte[3];
        try {
            TarUtils.formatLongOctalBytes(value3, buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("256=400 will not fit in octal number buffer of length 3", e.getMessage());
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // typical use case
        long value1 = 123;
        byte[] buffer1 = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value1, buffer1, 0, buffer1.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '1', ' '}, buffer1);

        // edge case
        long value2 = 0;
        byte[] buffer2 = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value2, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{'0', '0', '0', '0', ' '}, buffer2);

        // error scenario
        long value3 = 256;
        byte[] buffer3 = new byte[3];
        try {
            TarUtils.formatCheckSumOctalBytes(value3, buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("256=400 will not fit in octal number buffer of length 3", e.getMessage());
        }
    }

    @Test
    public void testComputeCheckSum() {
        // typical use case
        byte[] buffer1 = {'h', 'e', 'l', 'l', 'o'};
        assertEquals(532, TarUtils.computeCheckSum(buffer1));

        // edge case
        byte[] buffer2 = new byte[0];
        assertEquals(0, TarUtils.computeCheckSum(buffer2));
    }
}