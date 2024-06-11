
import org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // ... (previously written test case)
    }

    @Test
    public void testToStringEmptyArchiveEntry() {
        // Test with empty ArchiveEntry
    }

    @Test
    public void testToStringNullArchiveEntry() {
        // Test with null ArchiveEntry
    }

    @Test
    public void testMatchAsciiBufferNullExpectedString() {
        // Test with null expected string
    }

    @Test
    public void testMatchAsciiBufferNullBuffer() {
        // Test with null buffer
    }

    @Test
    public void testMatchAsciiBufferInvalidOffsetOrLength() {
        // Test with invalid offset or length
    }

    @Test
    public void testMatchAsciiBufferBufferLengthSmallerThanStringLength() {
        // Test with buffer length smaller than string length
    }

    @Test
    public void testMatchAsciiBufferBufferContainsNonAsciiCharacters() {
        // Test with buffer containing non-ASCII characters
    }

    @Test
    public void testMatchAsciiBufferStringIsNull() {
        // Test with null string
    }

    @Test
    public void testMatchAsciiBufferBufferIsNull() {
        // Test with null buffer
    }

    @Test
    public void testToAsciiBytesNullInputString() {
        // Test with null inputString
    }

    @Test
    public void testToAsciiBytesInputStringLongerThanIntMaxBytes() {
        // Test with inputString longer than INT_MAX bytes
    }

    @Test
    public void testToAsciiStringNullByteArray() {
        // Test with null byte array
    }

    @Test
    public void testToAsciiStringInvalidOffsetOrLength() {
        // Test with invalid offset or length
    }

    @Test
    public void testToAsciiStringByteArrayContainsNonAsciiCharacters() {
        // Test with byte array containing non-ASCII characters
    }

    @Test
    public void testIsEqualNullBuffer1() {
        // Test with null buffer1
    }

    @Test
    public void testIsEqualNullBuffer2() {
        // Test with null buffer2
    }

    @Test
    public void testIsEqualDifferentLengths() {
        // Test with different lengths
    }

    @Test
    public void testIsEqualDifferentContentsWithIgnoreTrailingNulls() {
        // Test with different contents but ignoreTrailingNulls is true
    }

    @Test
    public void testIsEqualDifferentContentsWithoutIgnoreTrailingNulls() {
        // Test with different contents but ignoreTrailingNulls is false
    }

    @Test
    public void testIsEqualWithNullNullBuffer1() {
        // Test with null buffer1
    }

    @Test
    public void testIsEqualWithNullNullBuffer2() {
        // Test with null buffer2
    }

    @Test
    public void testIsEqualWithNullDifferentLengths() {
        // Test with null buffer1 and different lengths
    }

    @Test
    public void testIsEqualWithNullDifferentContents() {
        // Test with null buffer1 and different contents
    }

    @Test
    public void testIsEqualWithNullDifferentContentsButIgnoreTrailingNulls() {
        // Test with null buffer1 and different contents but ignoreTrailingNulls is true
    }

    @Test
    public void testIsArrayZeroNullArray() {
        // Test with null array
    }

    @Test
    public void testIsArrayZeroEmptyArray() {
        // Test with empty array
    }

    @Test
    public void testIsArrayZeroArrayHasNonZeroBytes() {
        // Test with array having non-zero bytes
    }

    @Test
    public void testSanitizeNullInputString() {
        // Test with null input string
    }

    @Test
    public void testSanitizeEmptyString() {
        // Test with empty string
    }

    @Test
    public void testSanitizeStringLongerThan255Characters() {
        // Test with string longer than 255 characters
    }

}
