import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test case with a regular file
        ArchiveEntry fileEntry = new ArchiveEntry(false, 2000, "main.c");
        String expected1 = "-    2000 main.c";
        assertEquals(expected1, ArchiveUtils.toString(fileEntry));

        // Test case with a directory
        ArchiveEntry dirEntry = new ArchiveEntry(true, 100, "testfiles");
        String expected2 = "d     100 testfiles";
        assertEquals(expected2, ArchiveUtils.toString(dirEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test case with matching buffer
        String expected = "ASCII";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));

        // Test case with non-matching buffer
        String nonExpected = "NON-ASCII";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, nonExpected.getBytes(), 0, nonExpected.length()));
    }

    @Test
    public void testToAsciiBytes() {
        // Test case with a regular string
        String inputString = "Test String";
        byte[] expectedBytes = inputString.getBytes();
        byte[] resultBytes = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void testToAsciiString() {
        // Test case with a regular string
        String inputString = "Test String";
        byte[] inputBytes = inputString.getBytes();
        String expectedString = inputString;
        String resultString = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expectedString, resultString);
    }

    @Test
    public void testIsEqual() {
        // Test case with equal buffers
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Test case with non-equal buffers
        byte[] buffer3 = {1, 2, 4};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithNull() {
        // Test case with equal buffers
        byte[] buffer1 = {1, 2, 3};
        byte[] buffer2 = {1, 2, 3};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        // Test case with non-equal buffers
        byte[] buffer3 = {1, 2, 4};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsArrayZero() {
        // Test case with array of all zeros
        byte[] zeroArray = {0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(zeroArray, zeroArray.length));

        // Test case with non-zero elements
        byte[] nonZeroArray = {0, 1, 2};
        assertFalse(ArchiveUtils.isArrayZero(nonZeroArray, nonZeroArray.length));
    }

    @Test
    public void testSanitize() {
        // Test case with a string containing non-printable characters
        String inputString = "Sanitize\nString";
        String expectedString = "Sanitize?String";
        String resultString = ArchiveUtils.sanitize(inputString);
        assertEquals(expectedString, resultString);

        // Test case with a string containing special Unicode characters
        String inputString2 = "Sanitize☆String";
        String expectedString2 = "Sanitize?String";
        String resultString2 = ArchiveUtils.sanitize(inputString2);
        assertEquals(expectedString2, resultString2);
    }

}