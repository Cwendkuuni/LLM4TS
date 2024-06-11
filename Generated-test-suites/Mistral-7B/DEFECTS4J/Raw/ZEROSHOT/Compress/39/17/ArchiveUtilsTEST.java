import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        String expected = "-\t0\t test.txt";
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

    @Test
    public void testMatchAsciiBufferFalse() {
        String expected = "test";
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[]{84, 101, 116, 101, 115};
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{84, 101, 116, 101, 115};
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 5};
        byte[] buffer2 = new byte[]{5, 4, 3, 2, 1};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);

        byte[] buffer3 = new byte[]{1, 2, 3, 4, 5};
        byte[] buffer4 = new byte[]{1, 2, 3, 4, 5};
        result = ArchiveUtils.isEqual(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length, false);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 4, 5};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);

        byte[] buffer3 = new byte[]{1, 2, 3, 4, 5, 0};
        byte[] buffer4 = new byte[]{1, 2, 3, 4, 5, 0};
        result = ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(array, 0);
        Assert.assertTrue(result);

        byte[] array1 = new byte[]{0, 0, 0};
        result = ArchiveUtils.isArrayZero(array1, array1.length);
        Assert.assertTrue(result);

        byte[] array2 = new byte[]{1, 1, 1};
        result = ArchiveUtils.isArrayZero(array2, array2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "test\u0000\u0001\u007Ftest";
        String expected = "test????test";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }
}