
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test case 1: empty string should return empty string
        String result1 = metaphone.metaphone("");
        assertEquals("", result1);
        
        // Test case 2: single character should return same character
        String result2 = metaphone.metaphone("a");
        assertEquals("A", result2);
        
        // Test case 3: word with only vowels should return itself
        String result3 = metaphone.metaphone("aeiou");
        assertEquals("AEIOU", result3);
        
        // Test case 4: word with only consonants should return same characters
        String result4 = metaphone.metaphone("bcdfghjklmnpqrstvwxyz");
        assertEquals("BCDFGHJKLMNPQRSTVWXYZ", result4);
        
        // Test case 5: word with initial 2 character exceptions
        String result5 = metaphone.metaphone("kn");
        assertEquals("N", result5);
        
        String result6 = metaphone.metaphone("ae");
        assertEquals("A", result6);
        
        String result7 = metaphone.metaphone("wh");
        assertEquals("W", result7);
        
        String result8 = metaphone.metaphone("wr");
        assertEquals("R", result8);
        
        String result9 = metaphone.metaphone("x");
        assertEquals("S", result9);
        
        // Add more test cases here...
    }
    
    // Add more test methods for other public methods in the Metaphone class
    
    // Test case for encode() method
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        String result = (String) metaphone.encode("test");
        assertEquals("TST", result);
    }
    
    // Test case for isMetaphoneEqual() method
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        boolean result1 = metaphone.isMetaphoneEqual("test", "tist");
        assertEquals(true, result1);
        
        boolean result2 = metaphone.isMetaphoneEqual("test", "best");
        assertEquals(false, result2);
    }
}

