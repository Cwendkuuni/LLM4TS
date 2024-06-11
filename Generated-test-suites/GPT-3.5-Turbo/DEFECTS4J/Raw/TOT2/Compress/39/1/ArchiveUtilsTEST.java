import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("testFile.txt", false, 100);
        String expected = "-    100 testFile.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        // Edge case - directory entry
        ArchiveEntry directoryEntry = new ArchiveEntry("folder", true, 0);
        String expectedDirectory = "d     0 folder";
        assertEquals(expectedDirectory, ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "test";
        byte[] buffer = expected.getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case - mismatch
        String expectedMismatch = "notMatching";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expectedMismatch, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        String inputString = "test";
        byte[] expectedBytes = inputString.getBytes(CharsetNames.US_ASCII);
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        // Typical use case
        String inputString = "test";
        byte[] inputBytes = inputString.getBytes(CharsetNames.US_ASCII);
        assertEquals(inputString, ArchiveUtils.toAsciiString(inputBytes));

        // Edge case - offset and length
        String expectedSubstring = "est";
        assertEquals(expectedSubstring, ArchiveUtils.toAsciiString(inputBytes, 1, 3));
    }

    @Test
    public void testIsEqual() {
        // Typical use case
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Edge case - different content
        byte[] buffer3 = "different".getBytes(CharsetNames.US_ASCII);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithNull() {
        // Typical use case
        byte[] buffer1 = "test".getBytes(CharsetNames.US_ASCII);
        byte[] buffer2 = "test\0\0".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer2, 0, 6));

        // Edge case - same content with extra nulls
        byte[] buffer3 = "test\0\0".getBytes(CharsetNames.US_ASCII);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 4, buffer3, 0, 6));
    }

    @Test
    public void testIsArrayZero() {
        // Typical use case
        byte[] array = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));

        // Edge case - non-zero
        byte[] nonZeroArray = new byte[]{0, 1, 0, 1, 0};
        assertFalse(ArchiveUtils.isArrayZero(nonZeroArray, 5));
    }

    @Test
    public void testSanitize() {
        // Typical use case
        String input = "te\u0000st";
        String expected = "te?st";
        assertEquals(expected, ArchiveUtils.sanitize(input));

        // Edge case - long string
        String longInput = "long\u0003string\u001Fthat\u007Fsneeds\u000Dsanitizing";
        String longExpected = "long?string?that?sneeds?sanitizing";
        assertEquals(longExpected, ArchiveUtils.sanitize(longInput));
    }

}