import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone m = new Metaphone();
        
        // Test typical use case
        assertEquals("SB", m.metaphone("sub"));
        
        // Test edge case
        assertEquals("", m.metaphone(""));
        
        // Test error scenario
        assertEquals("", m.metaphone(null));
    }
    
    @Test
    public void testEncode() {
        Metaphone m = new Metaphone();
        
        // Test typical use case
        assertEquals("MTF", m.encode("metaphone"));
        
        // Test edge case
        assertEquals("", m.encode(""));
        
        // Test error scenario
        try {
            m.encode(123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone m = new Metaphone();
        
        // Test typical use case with equal strings
        assertTrue(m.isMetaphoneEqual("cat", "kat"));
        
        // Test typical use case with different strings 
        assertFalse(m.isMetaphoneEqual("cat", "dog"));
        
        // Test edge case
        assertTrue(m.isMetaphoneEqual("", ""));
        
        // Test error scenario
        try {
            m.isMetaphoneEqual("cat", 123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone m = new Metaphone();
        
        // Test default maxCodeLen value
        assertEquals(4, m.getMaxCodeLen());
        
        // Test setting maxCodeLen value
        m.setMaxCodeLen(3);
        assertEquals(3, m.getMaxCodeLen());
    }

}