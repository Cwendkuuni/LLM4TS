import org.junit.*;
import static org.junit.Assert.*;

public class Utf8Test {
    
    @Test
    public void testEncodedLength_typicalCase() {
        String sequence = "hello";
        int expectedLength = 5;
        
        int actualLength = Utf8.encodedLength(sequence);
        
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_edgeCase_singleCharacter() {
        String sequence = "a";
        int expectedLength = 1;
        
        int actualLength = Utf8.encodedLength(sequence);
        
        assertEquals(expectedLength, actualLength);
    }
    
    @Test
    public void testEncodedLength_errorScenario() {
        String sequence = "\udffe hello"; // unpaired surrogate at index 0
        try {
            Utf8.encodedLength(sequence);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Unpaired surrogate at index 0";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
    
    @Test
    public void testIsWellFormed_typicalCase_wellFormedBytes() {
        byte[] bytes = {0b01111111, 0b01010101, 0b01100001, 0b01111000, 0b01011111}; // well-formed UTF-8 bytes
        
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        
        assertTrue(isWellFormed);
    }
    
    @Test
    public void testIsWellFormed_typicalCase_malformedBytes() {
        byte[] bytes = {0b11111111, 0b01010101, 0b01100001, 0b01111000, 0b01011111}; // malformed UTF-8 bytes
        
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        
        assertFalse(isWellFormed);
    }
    
    @Test
    public void testIsWellFormed_edgeCase_emptyBytes() {
        byte[] bytes = new byte[0];
        
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        
        assertTrue(isWellFormed);
    }
    
    @Test
    public void testIsWellFormed_errorScenario_negativeByte() {
        byte[] bytes = {0b11111111, 0b01010101, (byte)-1, 0b01111000, 0b01011111}; // negative byte
        
        boolean isWellFormed = Utf8.isWellFormed(bytes);
        
        assertFalse(isWellFormed);
    }
}