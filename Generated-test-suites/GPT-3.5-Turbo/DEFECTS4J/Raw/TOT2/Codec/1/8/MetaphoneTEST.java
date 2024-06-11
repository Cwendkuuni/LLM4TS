import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("AMNT", metaphone.metaphone("amount"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(null)); // Null input
        assertEquals("TNTL", metaphone.metaphone("eternal")); // Single character input
        assertEquals("MT", metaphone.metaphone("m")); // Single character input
        
        // Error scenarios
        assertEquals("XKX", metaphone.metaphone("xkx")); // Mixing lower and upper case
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("FLKL", metaphone.encode("flicker"));
        
        // Edge cases
        assertEquals("T", metaphone.encode("t")); // Single character input
        assertEquals("", metaphone.encode(null)); // Null input
        
        // Error scenario
        try {
            metaphone.encode(123); // Non-string input
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected behavior
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("hello", "hall"));

        // Edge case
        assertFalse(metaphone.isMetaphoneEqual("world", "worm"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Edge case
        metaphone.setMaxCodeLen(8);
        assertEquals(8, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
        
        // Edge case
        metaphone.setMaxCodeLen(10);
        assertEquals(10, metaphone.getMaxCodeLen());
    }

}