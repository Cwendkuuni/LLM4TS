
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_emptyString_returnZero() {
        String emptyString = "";
        int result = Utf8.encodedLength(emptyString);
        assertEquals(0, result);
    }
    
    @Test
    public void testEncodedLength_singleCharASCII_returnOne() {
        String singleChar = "A";
        int result = Utf8.encodedLength(singleChar);
        assertEquals(1, result);
    }
    
    @Test
    public void testEncodedLength_singleCharNonASCII_returnTwo() {
        String singleChar = "é";
        int result = Utf8.encodedLength(singleChar);
        assertEquals(2, result);
    }
    
    @Test
    public void testEncodedLength_fullASCIIString_returnLength() {
        String asciiString = "Hello World";
        int result = Utf8.encodedLength(asciiString);
        assertEquals(asciiString.length(), result);
    }
    
    @Test
    public void testEncodedLength_fullNonASCIIString_returnLength() {
        String nonAsciiString = "你好世界";
        int result = Utf8.encodedLength(nonAsciiString);
        assertEquals(nonAsciiString.length(), result);
    }
    
    @Test
    public void testEncodedLength_invalidSurrogatePair_throwIllegalArgumentException() {
        String invalidSurrogatePair = "abc\uD83D";
        assertThrows(IllegalArgumentException.class, () -> Utf8.encodedLength(invalidSurrogatePair));
    }
    
    @Test
    public void testIsWellFormed_emptyByteArray_returnTrue() {
        byte[] emptyByteArray = new byte[0];
        boolean result = Utf8.isWellFormed(emptyByteArray);
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_singleByteASCII_returnTrue() {
        byte[] singleByte = new byte[] { 65 };
        boolean result = Utf8.isWellFormed(singleByte);
        assertTrue(result);
    }
    
    @Test
    public void testIsWellFormed_singleByteInvalid_returnFalse() {
        byte[] singleByte = new byte[] { -1 };
        boolean result = Utf8.isWellFormed(singleByte);
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_invalidUTF8Sequence_returnFalse() {
        byte[] invalidSequence = new byte[] { -62, -64 };
        boolean result = Utf8.isWellFormed(invalidSequence);
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_partialInvalidUTF8Sequence_returnFalse() {
        byte[] partialInvalidSequence = new byte[] { -62, -64, 65 };
        boolean result = Utf8.isWellFormed(partialInvalidSequence);
        assertFalse(result);
    }
    
    @Test
    public void testIsWellFormed_invalidSurrogatePair_returnFalse() {
        byte[] invalidSurrogatePair = new byte[] { -16, -97, -128, -128 };
        boolean result = Utf8.isWellFormed(invalidSurrogatePair);
        assertFalse(result);
    }
}

