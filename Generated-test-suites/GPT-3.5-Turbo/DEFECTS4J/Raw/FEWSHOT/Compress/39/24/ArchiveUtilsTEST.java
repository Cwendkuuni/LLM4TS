import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setIsDirectory(true);
        entry1.setSize(100);
        entry1.setName("file1");

        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setIsDirectory(false);
        entry2.setSize(2000);
        entry2.setName("file2");

        String result1 = ArchiveUtils.toString(entry1);
        String result2 = ArchiveUtils.toString(entry2);

        Assert.assertEquals("d     100 file1", result1);
        Assert.assertEquals("-    2000 file2", result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        String expected = "test";
        byte[] buffer = expected.getBytes();
        int offset = 1;
        int length = 2;

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);

        Assert.assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        Assert.assertArrayEquals(result, inputString.getBytes());
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();

        String result = ArchiveUtils.toAsciiString(inputBytes);

        Assert.assertEquals("test", result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        int offset = 1;
        int length = 2;

        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);

        Assert.assertEquals("es", result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        int offset1 = 1;
        int length1 = 2;
        int offset2 = 1;
        int length2 = 2;

        boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0\0".getBytes();
        byte[] buffer2 = "test".getBytes();
        boolean ignoreTrailingNulls = true;

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, ignoreTrailingNulls);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsWithNulls() {
        byte[] buffer1 = "test\0\0".getBytes();
        byte[] buffer2 = "test".getBytes();
        boolean ignoreTrailingNulls = true;

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, ignoreTrailingNulls);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLength() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "testlonger".getBytes();
        boolean ignoreTrailingNulls = true;

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, ignoreTrailingNulls);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLengths() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "testlonger".getBytes();
        boolean ignoreTrailingNulls = true;

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, ignoreTrailingNulls);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 0, 0, 0, 0};
        int size = 5;

        boolean result = ArchiveUtils.isArrayZero(a, size);

        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "hello\u0000world";

        String result = ArchiveUtils.sanitize(s);

        Assert.assertEquals("hello?world", result);
    }

}