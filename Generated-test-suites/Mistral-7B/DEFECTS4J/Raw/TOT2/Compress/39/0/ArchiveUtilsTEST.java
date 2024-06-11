import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.apache.commons.compress.utils.ArchiveUtils.toString;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        archiveEntry.setSize(1024L);
        archiveEntry.setDirectory(false);

        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "- 1024 test.txt";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected.toUpperCase());
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = toAsciiBytes("test123");
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[3];
        System.arraycopy(buffer1, 0, buffer3, 0, 3);
        buffer3[3] = 0; // add a trailing null
        boolean result2 = ArchiveUtils.isEqual(buffer1, buffer3, true); // ignore trailing nulls
        Assert.assertTrue(result2);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testUnsupportedEncodingException() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);
        ArchiveUtils.matchAsciiBuffer("", buffer, 0, buffer.length - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualWithNull_FirstOffsetOutOfBounds() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = new byte[3];
        ArchiveUtils.isEqual(buffer1, buffer2, -1, 3, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualWithNull_SecondOffsetOutOfBounds() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = new byte[3];
        ArchiveUtils.isEqual(buffer1, buffer2, 0, -1, false);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = new byte[4];
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, 3, false);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[4];
        System.arraycopy(buffer1, 0, buffer3, 0, buffer1.length);
        buffer3[buffer1.length] = 0; // add a trailing null
        boolean result2 = ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length, false);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIsEqualWithNull_FirstArrayZero() {
        byte[] buffer1 = new byte[0];
        byte[] buffer2 = toAsciiBytes("test123");
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull_SecondArrayZero() {
        byte[] buffer1 = toAsciiBytes("test123");
        byte[] buffer2 = new byte[0];
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull_BothArraysZero() {
        byte[] buffer1 = new byte[0];
        byte[] buffer2 = new byte[0];
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "test\u0000\u001F\u007F\u0080\u00FF";
        String result = ArchiveUtils.sanitize(input);
        String expected = "test?????";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSanitizeLongString() {
        String input = "This is a very long test string that will be truncated if it's not sanitized properly.";
        String result = ArchiveUtils.sanitize(input);
        int maxLength = 255;
        Assert.assertEquals("This is a very long test str...", result.substring(0, maxLength));
        Assert.assertTrue(result.length() > maxLength);
    }

    @Test
    public void testToStringWithLargeEntrySize() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        archiveEntry.setSize(Long.MAX_VALUE);
        archiveEntry.setDirectory(false);

        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "- 9223372036854775807 test.txt"; // Long.MAX_VALUE in decimal
        Assert.assertEquals(expected, result);
    }

}