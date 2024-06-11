import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("test.txt");
        ArchiveEntry directoryEntry = new ArchiveEntry("test/");
        String toStringResult = ArchiveUtils.toString(archiveEntry);
        String directoryToStringResult = ArchiveUtils.toString(directoryEntry);

        Assert.assertEquals("- 10 test.txt", toStringResult);
        Assert.assertEquals("d 0 test/", directoryToStringResult);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = toAsciiBytes(expected);

        boolean matchResult = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(matchResult);

        byte[] differentBuffer = Arrays.copyOfRange(buffer, 0, 3);
        Arrays.fill(differentBuffer, (byte) 1, buffer.length, (byte) 0);

        boolean noMatchResult = ArchiveUtils.matchAsciiBuffer(expected, differentBuffer);
        Assert.assertFalse(noMatchResult);
    }

    @Test
    public void testIsEqual() {
        byte[] firstBuffer = toAsciiBytes("test");
        byte[] secondBuffer = toAsciiBytes("test");

        boolean equalsResult = ArchiveUtils.isEqual(firstBuffer, 0, firstBuffer.length, secondBuffer, 0, secondBuffer.length, false);
        Assert.assertTrue(equalsResult);

        byte[] differentBuffer = Arrays.copyOfRange(firstBuffer, 0, 3);
        Arrays.fill(differentBuffer, (byte) 1, firstBuffer.length, (byte) 0);

        boolean noEqualsResult = ArchiveUtils.isEqual(firstBuffer, 0, firstBuffer.length, differentBuffer, 0, differentBuffer.length, false);
        Assert.assertFalse(noEqualsResult);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] firstBuffer = toAsciiBytes("test");
        byte[] secondBuffer = toAsciiBytes("test");
        byte[] firstNullTrailingBuffer = Arrays.copyOfRange(firstBuffer, 0, firstBuffer.length + 1);
        Arrays.fill(firstNullTrailingBuffer, (byte) 0, firstNullTrailingBuffer.length, (byte) 0);

        boolean equalsWithNullResult = ArchiveUtils.isEqualWithNull(firstBuffer, 0, firstBuffer.length, secondBuffer, 0, secondBuffer.length);
        Assert.assertTrue(equalsWithNullResult);

        byte[] secondNullTrailingBuffer = Arrays.copyOfRange(secondBuffer, 0, secondBuffer.length + 1);
        Arrays.fill(secondNullTrailingBuffer, (byte) 0, secondNullTrailingBuffer.length, (byte) 0);

        boolean equalsWithBothNullTrailingResult = ArchiveUtils.isEqualWithNull(firstBuffer, 0, firstBuffer.length, secondBuffer, 0, secondBuffer.length);
        Assert.assertTrue(equalsWithBothNullTrailingResult);

        byte[] notEqualBuffer = Arrays.copyOfRange(firstBuffer, 0, 3);
        Arrays.fill(notEqualBuffer, (byte) 1, firstBuffer.length, (byte) 0);

        boolean noEqualsWithNullResult = ArchiveUtils.isEqualWithNull(firstBuffer, 0, firstBuffer.length, notEqualBuffer, 0, notEqualBuffer.length);
        Assert.assertFalse(noEqualsWithNullResult);
    }

    @Test
    public void testArrayZero() {
        byte[] zeroArray = new byte[10];
        Arrays.fill(zeroArray, (byte) 0);

        boolean isZeroResult = ArchiveUtils.isArrayZero(zeroArray, zeroArray.length);
        Assert.assertTrue(isZeroResult);
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001\u001Atest\u0002";
        String sanitizedInput = ArchiveUtils.sanitize(input);

        Assert.assertEquals("test?test?", sanitizedInput);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithIllegalCharset() {
        ArchiveUtils.matchAsciiBuffer("test", new byte[10]);
    }

    @Test
    public void testToAsciiString() {
        byte[] byteArray = toAsciiBytes("test");
        String asciiString = ArchiveUtils.toAsciiString(byteArray);

        Assert.assertEquals("test", asciiString);
    }

    @Test
    public void testToAsciiStringWithOffset() {
        byte[] byteArray = toAsciiBytes("test");
        String asciiString = ArchiveUtils.toAsciiString(byteArray, 0, byteArray.length);

        Assert.assertEquals("test", asciiString);
    }
}