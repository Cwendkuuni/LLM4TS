import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        String result1 = metaphone.metaphone("Apache");
        assertEquals("APCH", result1);
        
        // Edge case
        String result2 = metaphone.metaphone("1234");
        assertEquals("", result2);
        
        // Error scenario
        String result3 = metaphone.metaphone(null);
        assertEquals("", result3);
    }
    
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        Object result1 = metaphone.encode("Apache");
        assertEquals("APCH", result1);
        
        // Error scenario
        try {
            Object result2 = metaphone.encode(1234);
            fail("EncoderException was not thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        boolean result1 = metaphone.isMetaphoneEqual("Apache", "Appache");
        assertTrue(result1);
        
        // Edge case
        boolean result2 = metaphone.isMetaphoneEqual("ABC", "DEF");
        assertFalse(result2);
        
        // Error scenario
        boolean result3 = metaphone.isMetaphoneEqual(null, "ABC");
        assertFalse(result3);
    }
    
}