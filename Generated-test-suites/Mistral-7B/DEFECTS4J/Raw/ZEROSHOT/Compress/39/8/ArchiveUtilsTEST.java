import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String expected = "- 2000 main.c";
        String result = ArchiveUtils.toString(entry);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "hello";
        byte[] buffer = new byte[5];
        Arrays.fill(buffer, (byte) 0x55);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("hello world");
        byte[] buffer2 = toAsciiBytes("hello world");

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[10];
        Arrays.fill(buffer3, (byte) 0x55);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        Assert.assertFalse(result);

        byte[] buffer4 = new byte[11];
        Arrays.fill(buffer4, (byte) 0x55);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[5];
        byte[] buffer2 = new byte[5];
        Arrays.fill(buffer1, (byte) 0x55);
        Arrays.fill(buffer2, (byte) 0x55);

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        Arrays.fill(buffer2, 6, buffer2.length, (byte) 0);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        Arrays.fill(buffer1, 2, buffer1.length, (byte) 0);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        Arrays.fill(buffer2, 4, buffer2.length, (byte) 0);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        Arrays.fill(a, (byte) 0);

        boolean result = ArchiveUtils.isArrayZero(a, a.length);
        Assert.assertTrue(result);

        result = ArchiveUtils.isArrayZero(a, a.length + 1);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001file\u001Fname";
        String expected = "test???file???name";
        String result = ArchiveUtils.sanitize(s);

        Assert.assertEquals(expected, result);
    }
}