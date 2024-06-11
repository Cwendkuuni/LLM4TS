import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertEquals("KNSN", metaphone.metaphone("kinsen"));
        assertEquals("LKSTR", metaphone.metaphone("Lancaster"));
        assertEquals("SYPG", metaphone.metaphone("SYPGI"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(null));
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("A", metaphone.metaphone("AaEeIiOoUu"));
        assertEquals("KW", metaphone.metaphone("q"));
        
        // Error scenarios
        assertEquals("R", metaphone.metaphone("34532+"));
        assertEquals("TST", metaphone.metaphone("Test1!"));
        assertEquals("WLKL", metaphone.metaphone("Walk&Lift"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertEquals("KNSN", metaphone.encode("kinsen"));
        assertEquals("LKSTR", metaphone.encode("Lancaster"));
        assertEquals("SYPG", metaphone.encode("SYPGI"));
        
        // Error scenarios
        try {
            metaphone.encode(12345);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertTrue(metaphone.isMetaphoneEqual("kinsen", "kinsen"));
        assertFalse(metaphone.isMetaphoneEqual("kinsen", "Lancaster"));
        assertTrue(metaphone.isMetaphoneEqual("SYPG", "SYPGI"));
        
        // Error scenarios
        assertFalse(metaphone.isMetaphoneEqual("kinsen", null));
        assertFalse(metaphone.isMetaphoneEqual(null, "Lancaster"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
    
}