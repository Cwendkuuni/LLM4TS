import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case: testing a word with vowels and consonants
        String result1 = metaphone.metaphone("hello");
        assertEquals("HL", result1);
        
        // Edge case: testing an empty string
        String result2 = metaphone.metaphone("");
        assertEquals("", result2);
        
        // Error scenario: testing a word with special characters
        String result3 = metaphone.metaphone("test!");
        assertEquals("TST", result3);
    }
    
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case: testing encoding a string
        Object result1 = metaphone.encode("hello");
        assertEquals("HL", result1);
        
        // Error scenario: testing encoding a non-string object
        try {
            metaphone.encode(123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            // Exception should be thrown
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case: testing two identical words
        boolean result1 = metaphone.isMetaphoneEqual("hello", "hello");
        assertTrue(result1);
        
        // Typical use case: testing two different words
        boolean result2 = metaphone.isMetaphoneEqual("hello", "world");
        assertFalse(result2);
        
        // Typical use case: testing one empty string and one non-empty string
        boolean result3 = metaphone.isMetaphoneEqual("", "hello");
        assertFalse(result3);
        
        // Error scenario: testing two null strings
        boolean result4 = metaphone.isMetaphoneEqual(null, null);
        assertTrue(result4);
    }
    
    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Default maxCodeLen should be 4
        int result1 = metaphone.getMaxCodeLen();
        assertEquals(4, result1);
    }
    
    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Set maxCodeLen to a different value
        metaphone.setMaxCodeLen(6);
        int result1 = metaphone.getMaxCodeLen();
        assertEquals(6, result1);
    }
}