import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.utils.ArchiveUtils.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("testfile.txt");
        String result = toString(entry);
        String expected = "-      0 testfile.txt";
        Assert.assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("testdir");
        result = toString(directoryEntry);
        expected = "d     0 testdir/";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);

        result = matchAsciiBuffer(expected, new byte[0]);
        Assert.assertFalse(result);

        result = matchAsciiBuffer("different", buffer);
        Assert.assertFalse(result);

        byte[] bufferWithLengthLessThanExpected = new byte[expected.length() - 1];
        result = matchAsciiBuffer(expected, bufferWithLengthLessThanExpected);
        Assert.assertFalse(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesWithUnsupportedEncodingException() {
        String inputString = new String(new byte[0], CharsetNames.US_ASCII); // empty string should not throw an exception
        byte[] result = toAsciiBytes(inputString);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 'D'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C', 'D'};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        buffer2 = new byte[]{'A', 'B', 'C', 'E'};
        result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);

        buffer2 = new byte[]{'A', 'B', 'C', null};
        result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertFalse(result);

        buffer2 = new byte[]{'A', 'B', 'C', null};
        result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', null};
        byte[] buffer2 = new byte[]{'A', 'B', 'C', null};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        boolean result = isArrayZero(array, 0);
        Assert.assertTrue(result);

        array = new byte[]{0};
        result = isArrayZero(array, 0);
        Assert.assertTrue(result);

        array = new byte[]{1, 2, 3};
        result = isArrayZero(array, 0);
        Assert.assertFalse(result);

        array = new byte[]{0, 0, 0};
        result = isArrayZero(array, 0);
        Assert.assertTrue(result);
    }

    @Test
    public void testSanitize() {
        String input = "test file with ñ, ä, æ and µ";
        String result = sanitize(input);
        String expected = "test file with ?', ?, ? and ?";
        Assert.assertEquals(expected, result);
    }
}