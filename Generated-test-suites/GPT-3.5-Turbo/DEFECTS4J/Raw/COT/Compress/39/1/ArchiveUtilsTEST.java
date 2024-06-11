
/* JUnit test cases for ArchiveUtils class */

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        String result = ArchiveUtils.toString(entry);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[]{116, 101, 115, 116};

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        Assert.assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[]{116, 101, 115, 116};

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{116, 101, 115, 116};
        String expected = "test";

        String result = ArchiveUtils.toAsciiString(inputBytes);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{116, 101, 115, 116};
        byte[] buffer2 = new byte[]{116, 101, 115, 116};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[]{0, 0, 0, 0};
        int size = 4;

        boolean result = ArchiveUtils.isArrayZero(a, size);

        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "abc\ndef\tghi";

        String expected = "abc?def?ghi";
        String result = ArchiveUtils.sanitize(s);

        Assert.assertEquals(expected, result);
    }
}