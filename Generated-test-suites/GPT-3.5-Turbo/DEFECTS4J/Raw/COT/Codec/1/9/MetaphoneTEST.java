import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Basic functionality test
        String word1 = "cat";
        String word2 = "kitten";
        assertEquals("KT", metaphone.metaphone(word1));
        assertEquals("KTN", metaphone.metaphone(word2));
    }
    
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Basic functionality test
        String word1 = "cat";
        String word2 = "kitten";
        assertEquals("KT", metaphone.encode(word1));
        assertEquals("KTN", metaphone.encode(word2));
        
        // Exception handling test
        try {
            Object obj = new Object();
            metaphone.encode(obj);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Basic functionality test
        String word1 = "cat";
        String word2 = "kitten";
        String word3 = "kit";
        assertTrue(metaphone.isMetaphoneEqual(word1, word2));
        assertFalse(metaphone.isMetaphoneEqual(word1, word3));
    }
    
    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test default maxCodeLen
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Test setting maxCodeLen
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }
}