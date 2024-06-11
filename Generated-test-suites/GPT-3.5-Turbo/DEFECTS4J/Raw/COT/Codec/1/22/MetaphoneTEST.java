
import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test empty string
        assertEquals("", metaphone.metaphone(""));
        
        // Test single character
        assertEquals("A", metaphone.metaphone("a"));
        
        // Test two characters
        assertEquals("HK", metaphone.metaphone("hk"));
        
        // Test word with special characters
        assertEquals("TTL", metaphone.metaphone("Test!@#This&"));
        
        // Test word with upper case characters
        assertEquals("SSK", metaphone.metaphone("TesT"));
        
        // Test word with spaces
        assertEquals("KNCN", metaphone.metaphone(" King of Crystal"));
        
    }
    
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Test encode with string input
        assertEquals("KM", metaphone.encode("came"));
        
        // Test encode with non-string input
        try {
            metaphone.encode(new Integer(5));
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Test equal metaphones
        assertTrue(metaphone.isMetaphoneEqual("cat", "ket"));
        
        // Test non-equal metaphones
        assertFalse(metaphone.isMetaphoneEqual("listen", "silent"));
    }
    
    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test default max code length
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Test setting max code length
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
}

