import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        String expected = "-       10 test.txt";
        String result = ArchiveUtils.toString(archiveEntry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithUnsupportedEncodingException() {
        String expected = "test";
        byte[] buffer = "test".getBytes(CharsetNames.UTF_8); // Unsupported encoding
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithTrailingNulls() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't', 0};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 0};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithDifferentData() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 'f'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = new byte[]{'t', 'e', 's', 't'};
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{'t', 'e', 's', 't'};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "test";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001\u007Ftest";
        String expected = "test?test";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(a, 0);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZeroWithNonZeroData() {
        byte[] a = new byte[]{'t'};
        boolean result = ArchiveUtils.isArrayZero(a, 0);
        Assert.assertFalse(result);
    }

    @Test
    public void testToStringWithNullEntry() {
        ArchiveEntry archiveEntry = null;
        String result = ArchiveUtils.toString(archiveEntry);
        Assert.assertNull(result);
    }

    @Test
    public void testMatchAsciiBufferWithNullExpected() {
        byte[] buffer = new byte[0];
        String expected = null;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithEmptyBuffer() {
        String expected = "";
        byte[] buffer = new byte[0];
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithNullBuffer() {
        String expected = "test";
        byte[] buffer = null;
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 0);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullBuffer1() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{'t', 'e', 's'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 0, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullBuffer2() {
        byte[] buffer1 = new byte[]{'t', 'e', 's'};
        byte[] buffer2 = null;
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, 0);
        Assert.assertFalse(result);
    }

    @Test
    public void testToStringWithNullArchiveEntry() {
        ArchiveEntry archiveEntry = null;
        String result = ArchiveUtils.toString(archiveEntry);
        Assert.assertNull(result);
    }

    @Test
    public void testToStringWithEmptyArchiveEntryName() {
        ArchiveEntry archiveEntry = new ArchiveEntry("");
        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "-      0";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWithLargeArchiveEntryName() {
        String archiveEntryName = "A" + Arrays.asList(new String(new byte[5000])).stream().map(String::new).collect(Collectors.joining(""));
        ArchiveEntry archiveEntry = new ArchiveEntry(archiveEntryName);
        String result = ArchiveUtils.toString(archiveEntry);
        Assert.assertEquals(result.length(), 5003);
    }
}