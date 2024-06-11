import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0      test.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);
        byte[] wrongBuffer = Arrays.copyOfRange(buffer, 0, 5);
        ArchiveUtils.matchAsciiBuffer(expected, wrongBuffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'};
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', (byte) 0};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', 'E', 'N', 'D'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        boolean result = ArchiveUtils.isArrayZero(a, 10);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZeroWithNonZeroValues() {
        byte[] a = new byte[]{1, 2, 3, 4, 5};
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\u0000string";
        String result = ArchiveUtils.sanitize(s);
        String expected = "test????????string";
        assertEquals(expected, result);
    }
}