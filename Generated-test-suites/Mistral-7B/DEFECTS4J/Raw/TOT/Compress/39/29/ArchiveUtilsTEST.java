import org.apache.commons.compress.utils.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(10L);
        String expected = "d     10 test.txt\n";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);

        entry = new ArchiveEntry("test.dir/");
        entry.setSize(0L);
        expected = "d     0 test.dir/\n";
        result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);

        entry = new ArchiveEntry("test.dir/test.txt");
        entry.setSize(20L);
        entry.setDirectory(true);
        expected = "-      20 test.dir/test.txt\n";
        result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        final String expected = "test";
        final byte[] buffer1 = ArchiveUtils.toAsciiBytes(expected);
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};

        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer1));
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer2));

        final byte[] buffer3 = new byte[]{'t', 'e', 's', 't', 0};
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer3));
    }

    @Test
    public void testIsEqual() {
        final byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer3 = new byte[]{'t', 'e', 's', 't', 0};

        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        final byte[] buffer4 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer5 = new byte[]{'t', 'e', 's', 't', 't'};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length));
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length));
    }

    @Test
    public void testIsEqualWithNull() {
        final byte[] buffer1 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't'};
        final byte[] buffer3 = new byte[]{'t', 'e', 's', 't', 0};
        final byte[] buffer4 = new byte[]{'t', 'e', 's', 't', 0, 0};

        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length));
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, false));

        final byte[] buffer5 = new byte[]{'t', 'e', 's', 't', 0, 0, 0};
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length));
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length, false));
    }

    @Test
    public void testSanitize() {
        final String string1 = "test\u0001\u007Ftest\u007F";
        final String string2 = "\u007Ftest\u0001test\u007F";
        final String expected = "test?test?";
        String result = ArchiveUtils.sanitize(string1);
        Assert.assertEquals(expected, result);

        result = ArchiveUtils.sanitize(string2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsArrayZero() {
        final byte[] array = new byte[0];
        final byte[] array1 = new byte[]{0};
        final byte[] array2 = new byte[]{0, 0, 0};
        final byte[] array3 = new byte[]{0, 'a'};

        Assert.assertTrue(ArchiveUtils.isArrayZero(array, 0));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array, array.length));

        Assert.assertFalse(ArchiveUtils.isArrayZero(array1, 0));
        Assert.assertFalse(ArchiveUtils.isArrayZero(array1, array1.length));

        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 0));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, array2.length));

        Assert.assertFalse(ArchiveUtils.isArrayZero(array3, 0));
        Assert.assertFalse(ArchiveUtils.isArrayZero(array3, array3.length));
    }

    @Test
    public void testToAsciiString() {
        final byte[] buffer = new byte[]{'t', 'e', 's', 't'};
        final String expected = "test";

        String result = ArchiveUtils.toAsciiString(buffer);
        Assert.assertEquals(expected, result);

        result = ArchiveUtils.toAsciiString(buffer, 1, buffer.length - 1);
        Assert.assertEquals(expected, result);

        final byte[] buffer2 = new byte[]{'t', 'e', 's', 't', 0};
        result = ArchiveUtils.toAsciiString(buffer2);
        Assert.assertEquals(expected, result);

        result = ArchiveUtils.toAsciiString(buffer2, 0, buffer2.length - 1);
        Assert.assertEquals(expected, result);
    }
}