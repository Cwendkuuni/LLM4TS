import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test case 1
        byte[] buffer1 = "123456\0".getBytes();
        int offset1 = 0;
        int length1 = 7;
        assertEquals(3423910, TarUtils.parseOctal(buffer1, offset1, length1));

        // Test case 2
        byte[] buffer2 = "   789   \0".getBytes();
        int offset2 = 0;
        int length2 = 11;
        assertEquals(0471, TarUtils.parseOctal(buffer2, offset2, length2));

        // Test case 3
        byte[] buffer3 = "   000   \0".getBytes();
        int offset3 = 0;
        int length3 = 11;
        assertEquals(0, TarUtils.parseOctal(buffer3, offset3, length3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = "   0x1   \0".getBytes();
        int offset = 0;
        int length = 11;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = "Hello\0".getBytes();
        int offset1 = 0;
        int length1 = 6;
        assertEquals("Hello", TarUtils.parseName(buffer1, offset1, length1));

        byte[] buffer2 = "Goodbye\0".getBytes();
        int offset2 = 0;
        int length2 = 8;
        assertEquals("Goodbye", TarUtils.parseName(buffer2, offset2, length2));
    }

    @Test
    public void testFormatNameBytes() {
        String name1 = "Hello";
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = 6;
        int expectedOffset1 = 6;
        assertArrayEquals("Hello\0\0\0\0\0", buf1);
        assertEquals(expectedOffset1, TarUtils.formatNameBytes(name1, buf1, offset1, length1));

        String name2 = "Goodbye";
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 8;
        int expectedOffset2 = 8;
        assertArrayEquals("Goodbye\0", buf2);
        assertEquals(expectedOffset2, TarUtils.formatNameBytes(name2, buf2, offset2, length2));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value1 = 12345;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = 8;
        int expectedOffset1 = 8;
        assertArrayEquals("000012345".getBytes(), buf1);
        assertEquals(expectedOffset1, TarUtils.formatUnsignedOctalString(value1, buf1, offset1, length1));

        long value2 = 567890;
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 6;
        int expectedOffset2 = 6;
        assertArrayEquals("345678".getBytes(), buf2);
        assertEquals(expectedOffset2, TarUtils.formatUnsignedOctalString(value2, buf2, offset2, length2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidBufferLength() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buf, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value1 = 12345;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = 8;
        int expectedOffset1 = 8;
        assertArrayEquals("000012345 ".getBytes(), buf1);
        assertEquals(expectedOffset1, TarUtils.formatOctalBytes(value1, buf1, offset1, length1));

        long value2 = 567890;
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 6;
        int expectedOffset2 = 6;
        assertArrayEquals("345678 ".getBytes(), buf2);
        assertEquals(expectedOffset2, TarUtils.formatOctalBytes(value2, buf2, offset2, length2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesWithInvalidBufferLength() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value1 = 12345;
        byte[] buf1 = new byte[8];
        int offset1 = 0;
        int length1 = 7;
        int expectedOffset1 = 7;
        assertArrayEquals("0001234 ".getBytes(), buf1);
        assertEquals(expectedOffset1, TarUtils.formatLongOctalBytes(value1, buf1, offset1, length1));

        long value2 = 567890;
        byte[] buf2 = new byte[10];
        int offset2 = 0;
        int length2 = 9;
        int expectedOffset2 = 9;
        assertArrayEquals("000567890".getBytes(), buf2);
        assertEquals(expectedOffset2, TarUtils.formatLongOctalBytes(value2, buf2, offset2, length2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesWithInvalidBufferLength() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value1 = 12345;
        byte[] buf1 = new byte[10];
        int offset1 = 0;
        int length1 = 8;
        int expectedOffset1 = 8;
        assertArrayEquals("00001234 ".getBytes(), buf1);
        assertEquals(expectedOffset1, TarUtils.formatCheckSumOctalBytes(value1, buf1, offset1, length1));

        long value2 = 567890;
        byte[] buf2 = new byte[8];
        int offset2 = 0;
        int length2 = 6;
        int expectedOffset2 = 6;
        assertArrayEquals("345678 ".getBytes(), buf2);
        assertEquals(expectedOffset2, TarUtils.formatCheckSumOctalBytes(value2, buf2, offset2, length2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesWithInvalidBufferLength() {
        long value = 12345;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf1 = "Hello\0".getBytes();
        assertEquals(532, TarUtils.computeCheckSum(buf1));

        byte[] buf2 = "Goodbye\0".getBytes();
        assertEquals(669, TarUtils.computeCheckSum(buf2));
    }
}