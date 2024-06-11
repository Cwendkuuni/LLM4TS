
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class Utf8Test {
    private Utf8 utf8;
    
    @Before
    public void setUp() {
        utf8 = new Utf8();
    }
    
    @Test
    public void testEncodedLength() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLength(""));
        
        // Test with single character
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(1, Utf8.encodedLength("A"));
        assertEquals(1, Utf8.encodedLength("1"));
        
        // Test with multiple characters
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(3, Utf8.encodedLength("ABC"));
        assertEquals(3, Utf8.encodedLength("123"));
        
        // Test with special characters
        assertEquals(6, Utf8.encodedLength("@#$%^&"));
        
        // Test with maximum length
        assertEquals(266, Utf8.encodedLength(generateString(133)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthIllegalArgumentException() {
        Utf8.encodedLength(generateInvalidString());
    }
    
    @Test
    public void testIsWellFormed() {
        // Test with empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));
        
        // Test with well-formed byte array
        assertTrue(Utf8.isWellFormed(new byte[] {65, 66, 67, 48, 49, 50}));
        
        // Test with poorly-formed byte array
        assertFalse(Utf8.isWellFormed(new byte[] {65, -1, 67, -128, 49, 50}));
    }
    
    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test with empty byte array
        assertTrue(Utf8.isWellFormed(new byte[6], 0, 0));
        
        // Test with well-formed byte array
        assertTrue(Utf8.isWellFormed(new byte[] {65, 66, 67, 48, 49, 50}, 0, 6));
        
        // Test with poorly-formed byte array
        assertFalse(Utf8.isWellFormed(new byte[] {65, -1, 67, -128, 49, 50}, 0, 6));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormedIllegalArgumentException() {
        Utf8.isWellFormed(new byte[] {65, 66, 67, 48, 49, 50}, 2, 5);
    }
    
    @Test
    public void testIsWellFormedSlowPath() {
        // Test with well-formed byte array
        assertTrue(Utf8.isWellFormedSlowPath(new byte[] {65, 66, 67}, 0, 3));
        
        // Test with poorly-formed byte array
        assertFalse(Utf8.isWellFormedSlowPath(new byte[] {65, -1, 67}, 0, 3));
    }
    
    private String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
    
    private String generateInvalidString() {
        return "𠜎𠜱𠜑𠜱𠝹𠜱𠜱𠜱𠜱𠜱𠜱𠜱";
    }
}
