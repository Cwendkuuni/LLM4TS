import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.compress.utils.ArchiveUtils.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(100);
        entry.setDirectory(false);

        String result = toString(entry);
        String expected = "- \t100 \t test.txt";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "HelloWorld";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithInvalidEncoding() {
        String expected = "HelloWorld";
        byte[] buffer = new byte[10];

        matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 5};
        byte[] buffer2 = new byte[]{1, 2, 3, 4, 5};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLengths() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3, 4};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullTrailing() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 4, 0};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullTrailing2() {
        byte[] buffer1 = new byte[]{1, 2, 3, 4, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNullTrailing3() {
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[5];
        Arrays.fill(a, (byte) 0);

        boolean result = isArrayZero(a, 5);
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String s = "\u0000\u0001test\u007F";
        String result = sanitize(s);
        String expected = "test";
        Assert.assertEquals(expected, result);
    }
}