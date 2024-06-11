import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static org.apache.commons.compress.utils.ArchiveUtils.isArrayZero;
import static org.apache.commons.compress.utils.ArchiveUtils.matchAsciiBuffer;
import static org.apache.commons.compress.utils.ArchiveUtils.sanitize;
import static org.apache.commons.compress.utils.ArchiveUtils.toString;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        archiveEntry.setSize(10);
        archiveEntry.setDirectory(false);

        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "-       10 test.txt";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString_directory() {
        ArchiveEntry archiveEntry = new ArchiveEntry("testDir");
        archiveEntry.setSize(0);
        archiveEntry.setDirectory(true);

        String result = ArchiveUtils.toString(archiveEntry);
        String expected = "d             0 testDir/";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBuffer_invalidEncoding() {
        String expected = "test";
        byte[] buffer = new byte[10];

        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testMatchAsciiBuffer_notEqual() {
        String expected = "test";
        byte[] buffer = "wrong".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        boolean expectedResult = false;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testMatchAsciiBuffer_offset() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 3);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testMatchAsciiBuffer_length() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 2);
        boolean expectedResult = false;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = new byte[]{ 't', 'e', 's', 't' };
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{ 't', 'e', 's', 't' };
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString_offset() {
        byte[] inputBytes = new byte[]{ 't', 'e', 's', 't' };
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, 4);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't' };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't' };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual_offset() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't' };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't' };
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual_differentLengths() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't' };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't', 'a' };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        boolean expectedResult = false;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't', 0 };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't' };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls_differentLengths() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't', 0 };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't' };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual_ignoreTrailingNulls_differentValues() {
        byte[] buffer1 = new byte[]{ 't', 'e', 's', 't', 0 };
        byte[] buffer2 = new byte[]{ 't', 'e', 's', 't', 1 };
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        boolean expectedResult = false;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        boolean result = ArchiveUtils.isArrayZero(a, 0);
        boolean expectedResult = true;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIsArrayZero_nonZero() {
        byte[] a = new byte[]{ 1 };
        boolean result = ArchiveUtils.isArrayZero(a, 0);
        boolean expectedResult = false;

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSanitize() {
        String input = "test?123~!@#$%^&*()_+={[}]|\\:;\"'<,>.?/ ";
        String expected = "test????????????????????????????????????";
        String result = ArchiveUtils.sanitize(input);

        Assert.assertEquals(expected, result);
    }

}