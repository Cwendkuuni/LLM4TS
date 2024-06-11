import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test when the sequence contains only ASCII characters
        CharSequence sequence1 = "Hello World";
        int result1 = Utf8.encodedLength(sequence1);
        int expected1 = 11;
        assertEquals(expected1, result1);

        // Test when the sequence contains non-ASCII characters
        CharSequence sequence2 = "안녕하세요";
        int result2 = Utf8.encodedLength(sequence2);
        int expected2 = 15;
        assertEquals(expected2, result2);

        // Test when the sequence contains surrogate pairs
        CharSequence sequence3 = "𩸽";
        int result3 = Utf8.encodedLength(sequence3);
        int expected3 = 6;
        assertEquals(expected3, result3);
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Test when the sequence contains only ASCII characters
        CharSequence sequence1 = "Hello World";
        int start1 = 0;
        int result1 = Utf8.encodedLengthGeneral(sequence1, start1);
        int expected1 = 11;
        assertEquals(expected1, result1);

        // Test when the sequence contains non-ASCII characters
        CharSequence sequence2 = "안녕하세요";
        int start2 = 0;
        int result2 = Utf8.encodedLengthGeneral(sequence2, start2);
        int expected2 = 15;
        assertEquals(expected2, result2);

        // Test when the sequence contains surrogate pairs
        CharSequence sequence3 = "𩸽";
        int start3 = 0;
        int result3 = Utf8.encodedLengthGeneral(sequence3, start3);
        int expected3 = 6;
        assertEquals(expected3, result3);
    }

    @Test
    public void testIsWellFormed() {
        // Test when the byte array contains only well-formed UTF-8 characters
        byte[] bytes1 = {(byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x57, (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64};
        boolean result1 = Utf8.isWellFormed(bytes1);
        assertTrue(result1);

        // Test when the byte array contains invalid UTF-8 characters
        byte[] bytes2 = {(byte) 0xc0, (byte) 0xae, (byte) 0x8b};
        boolean result2 = Utf8.isWellFormed(bytes2);
        assertFalse(result2);
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test when the byte array contains only well-formed UTF-8 characters
        byte[] bytes1 = {(byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x57, (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64};
        int offset1 = 0;
        int length1 = bytes1.length;
        boolean result1 = Utf8.isWellFormed(bytes1, offset1, length1);
        assertTrue(result1);

        // Test when the byte array contains invalid UTF-8 characters
        byte[] bytes2 = {(byte) 0xc0, (byte) 0xae, (byte) 0x8b};
        int offset2 = 0;
        int length2 = bytes2.length;
        boolean result2 = Utf8.isWellFormed(bytes2, offset2, length2);
        assertFalse(result2);
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test when the byte array contains only well-formed UTF-8 characters
        byte[] bytes1 = {(byte) 0x48, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x20, (byte) 0x57, (byte) 0x6f, (byte) 0x72, (byte) 0x6c, (byte) 0x64};
        int offset1 = 0;
        int end1 = bytes1.length;
        boolean result1 = Utf8.isWellFormedSlowPath(bytes1, offset1, end1);
        assertTrue(result1);

        // Test when the byte array contains invalid UTF-8 characters
        byte[] bytes2 = {(byte) 0xc0, (byte) 0xae, (byte) 0x8b};
        int offset2 = 0;
        int end2 = bytes2.length;
        boolean result2 = Utf8.isWellFormedSlowPath(bytes2, offset2, end2);
        assertFalse(result2);
    }

}