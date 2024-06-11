import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() throws Exception {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        Assert.assertEquals("- 2000 main.c\n", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() throws Exception {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithUnsupportedEncoding() {
        String expected = "Hello World";
        byte[] buffer = new byte[10]; // insufficient buffer length
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testToAsciiString() throws Exception {
        byte[] inputBytes = toAsciiBytes("Hello World");
        Assert.assertEquals("Hello World", ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiStringWithUnsupportedEncoding() {
        byte[] inputBytes = new byte[10]; // insufficient byte array length
        ArchiveUtils.toAsciiString(inputBytes);
    }

    @Test
    public void testIsEqual() throws Exception {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithNull() throws Exception {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 'e'};
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithDifferentLengths() throws Exception {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', 'e', 'f'};
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffset() throws Exception {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 1, 10, buffer2, 0, 10));
    }

    @Test
    public void testIsArrayZero() throws Exception {
        byte[] a = new byte[10];
        Assert.assertFalse(ArchiveUtils.isArrayZero(a, 5));
        byte[] zeroArray = new byte[10];
        Arrays.fill(zeroArray, (byte) 0);
        Assert.assertTrue(ArchiveUtils.isArrayZero(zeroArray, 10));
    }

    @Test
    public void testSanitize() throws Exception {
        String s = "Hello World! ðŸ˜€\nThis is a test!";
        String sanitized = ArchiveUtils.sanitize(s);
        Assert.assertEquals("Hello World! ?   ????????  ??????????  ??????????\nThis is a test!", sanitized);
    }
}