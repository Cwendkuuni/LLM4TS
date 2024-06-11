import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.compress.utils.ArchiveUtils.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        archiveEntry.setSize(2000);
        archiveEntry.setDirectory(false);

        String result = toString(archiveEntry);

        String expected = "- 2000 main.c";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer_whenExpectedAndBufferAreIdentical_thenReturnTrue() {
        String expected = "expected";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_whenExpectedAndBufferAreDifferent_thenReturnFalse() {
        String expected = "expected";
        byte[] buffer = new byte[]{1, 2, 3};

        boolean result = matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytes_whenEncodingIsUnsupported_thenThrowException() {
        String inputString = "inputString";

        toAsciiBytes(inputString);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("inputString");

        String result = toAsciiString(inputBytes);

        String expected = "inputString";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual_whenBuffersAreDifferent_thenReturnFalse() {
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{4, 5, 6};

        boolean result = isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{1, 2, 0, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0, 4};

        boolean result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);

        buffer1 = new byte[]{1, 2, 3, 0, 4};
        buffer2 = new byte[]{1, 2, 3, 0, 4};

        result = isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];

        boolean result = isArrayZero(array, array.length);
        Assert.assertTrue(result);

        array = new byte[]{0};

        result = isArrayZero(array, array.length);
        Assert.assertTrue(result);

        array = new byte[]{0, 0};

        result = isArrayZero(array, array.length);
        Assert.assertTrue(result);

        array = new byte[]{1};

        result = isArrayZero(array, array.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String s = "!@#$%^&*()_+={}[];:<>|./?\\";
        String result = sanitize(s);

        String expected = "...............";
        Assert.assertEquals(expected, result);
    }
}