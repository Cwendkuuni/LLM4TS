import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarUtils;

public class TarUtilsTest {

    // Test cases for parseOctal method

    @Test
    public void testParseOctal_typicalCase() {
        byte[] buffer = {49, 50, 51}; // "123" in ASCII
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);
        
        assertEquals(83, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_invalidByte() {
        byte[] buffer = {49, 50, 55}; // "127" in ASCII, invalid byte
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    // Test cases for parseName method

    @Test
    public void testParseName_typicalCase() {
        byte[] buffer = {104, 101, 108, 108, 111, 0}; // "hello" in ASCII
        int offset = 0;
        int length = 6;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("hello", result);
    }

    @Test
    public void testParseName_withTrailingNull() {
        byte[] buffer = {104, 105, 0, 0, 0, 0}; // "hi" followed by trailing nulls
        int offset = 0;
        int length = 6;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("hi", result);
    }

    // Test cases for formatNameBytes method

    @Test
    public void testFormatNameBytes_typicalCase() {
        String name = "hello";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 10;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(10, result);
        assertArrayEquals(new byte[] {104, 101, 108, 108, 111, 0, 0, 0, 0, 0}, buf);
    }

    @Test
    public void testFormatNameBytes_nameLongerThanBuffer() {
        String name = "thisNameIsTooLong";
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;

        int result = TarUtils.formatNameBytes(name, buf, offset, length);

        assertEquals(8, result);
        assertArrayEquals(new byte[] {116, 104, 105, 115, 78, 97, 109, 101}, buf);
    }

    // Test cases for formatUnsignedOctalString method

    @Test
    public void testFormatUnsignedOctalString_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(new byte[] {0, 0, 49, 50, 51}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_valueTooLarge() {
        long value = 123456;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    // Test cases for formatOctalBytes method

    @Test
    public void testFormatOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[] {0, 49, 50, 51, 32}, buffer);
    }

    @Test
    public void testFormatOctalBytes_valueZero() {
        long value = 0;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[] {0, 48, 48, 48, 32}, buffer);
    }

    // Test cases for formatLongOctalBytes method

    @Test
    public void testFormatLongOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

        assertEquals(4, result);
        assertArrayEquals(new byte[] {49, 50, 51, 32}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytes_valueTooLarge() {
        long value = 123456;
        byte[] buffer = new byte[4];
        int offset = 0;
        int length = 4;

        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
    }

    // Test cases for formatCheckSumOctalBytes method

    @Test
    public void testFormatCheckSumOctalBytes_typicalCase() {
        long value = 123;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[] {0, 49, 50, 51, 32}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes_valueZero() {
        long value = 0;
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

        assertEquals(5, result);
        assertArrayEquals(new byte[] {0, 48, 48, 48, 32}, buffer);
    }

    // Test case for computeCheckSum method

    @Test
    public void testComputeCheckSum_typicalCase() {
        byte[] buf = {49, 50, 51}; // "123" in ASCII

        long result = TarUtils.computeCheckSum(buf);

        assertEquals(150, result);
    }

}