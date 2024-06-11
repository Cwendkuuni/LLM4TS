import org.junit.Test;

public class Utf8Test {
    
    @Test
    public void testEncodedLength_typicalCase() {
        CharSequence sequence = "Test";
        int expectedLength = 4;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_emptyString() {
        CharSequence sequence = "";
        int expectedLength = 0;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_unicodeCharacters() {
        CharSequence sequence = "¥€£";
        int expectedLength = 6;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_specialCharacters() {
        CharSequence sequence = "!@#$%^&*()_+{}[];':,.<>/?`~";
        int expectedLength = 25;
        int actualLength = Utf8.encodedLength(sequence);
        assertEquals(expectedLength, actualLength);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_invalidUtf8Length() {
        CharSequence sequence = "TestTestTestTestTestTestTestTestTestTestTestTest"; // Length exceeds maximum int value
        Utf8.encodedLength(sequence);
    }
    
    @Test
    public void testIsWellFormed_typicalCase() {
        byte[] bytes = { 84, 101, 115, 116 };
        boolean expectedWellFormed = true;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormed_emptyByteArray() {
        byte[] bytes = {};
        boolean expectedWellFormed = true;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormed_invalidByteValue() {
        byte[] bytes = { 84, -105, 115, 116 }; // Second byte is negative
        boolean expectedWellFormed = false;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormed_slowPathInvalidBytes() {
        byte[] bytes = { -32, -65, 65 }; // Invalid byte sequence: \xF0\xBF\x41
        boolean expectedWellFormed = false;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormed_slowPathEdgeCase() {
        byte[] bytes = { -62, -65 }; // Incomplete sequence: \xC0\x80
        boolean expectedWellFormed = false;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
    
    @Test
    public void testIsWellFormed_slowPathInvalidBytesOnBorder() {
        byte[] bytes = { -16, -96 }; // Invalid byte sequence: \xF0\xA0
        boolean expectedWellFormed = false;
        boolean actualWellFormed = Utf8.isWellFormed(bytes);
        assertEquals(expectedWellFormed, actualWellFormed);
    }
}