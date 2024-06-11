import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    // Test case for encodedLength method
    @Test
    public void testEncodedLength() {
        CharSequence testSequence = "test";
        int result = Utf8.encodedLength(testSequence);
        assertEquals(4, result);
    }

    // Test case for isWellFormed method
    @Test
    public void testIsWellFormed() {
        byte[] testBytes = {(byte)0x61, (byte)0x62, (byte)0x63};
        boolean result = Utf8.isWellFormed(testBytes);
        assertTrue(result);
    }
    
    // Additional test cases for edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_Exception() {
        CharSequence testSequence = "𐍈𐊊";
        Utf8.encodedLength(testSequence);
    }

    @Test
    public void testIsWellFormed_InvalidBytes() {
        byte[] testBytes = {(byte)0xFF, (byte)0x62, (byte)0x63};
        boolean result = Utf8.isWellFormed(testBytes);
        assertFalse(result);
    }
}