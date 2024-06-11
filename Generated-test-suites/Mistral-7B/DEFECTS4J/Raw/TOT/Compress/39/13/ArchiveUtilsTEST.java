import org.apache.commons.compress.archivers.test.TestArchiveFile;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveFile("test.txt").getEntry("test.txt");
        String expected = "- 123 test.txt";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes(CharsetNames.US_ASCII);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferFail() {
        String expected = "test";
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String input = "test";
        byte[] expected = new byte[]{'t', 'e', 's', 't'};
        byte[] result = ArchiveUtils.toAsciiBytes(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] input = new byte[]{'t', 'e', 's', 't'};
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualFail() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 0x01};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[]{'t', 'e', 's', 't'};
        byte[] buffer4 = new byte[]{'t', 'e', 's', 't', 0x01};
        result = ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        Assert.assertFalse(ArchiveUtils.isArrayZero(buffer, 10));

        byte[] buffer2 = new byte[10];
        Arrays.fill(buffer2, (byte) 0);
        Assert.assertTrue(ArchiveUtils.isArrayZero(buffer2, 10));

        byte[] buffer3 = new byte[10];
        Arrays.fill(buffer3, (byte) 0, 5, (byte) 1);
        Assert.assertFalse(ArchiveUtils.isArrayZero(buffer3, 10));

        byte[] buffer4 = new byte[10];
        Arrays.fill(buffer4, (byte) 0, 5, (byte) 1);
        Assert.assertTrue(ArchiveUtils.isArrayZero(buffer4, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001\u007ftest\u007F";
        String expected = "test?test?";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }
}