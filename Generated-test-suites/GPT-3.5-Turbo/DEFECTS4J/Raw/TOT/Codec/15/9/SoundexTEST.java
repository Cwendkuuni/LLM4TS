import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundex_DefaultMapping() {
        Soundex soundex = new Soundex();
        
        // Typical use cases
        String encoded1 = soundex.encode("Robert");
        assertEquals("R163", encoded1);
        
        String encoded2 = soundex.encode("Rupert");
        assertEquals("R163", encoded2);
        
        String encoded3 = soundex.encode("Rubin");
        assertEquals("R150", encoded3);
        
        // Edge cases
        String encoded4 = soundex.encode("R");
        assertEquals("R000", encoded4);
        
        String encoded5 = soundex.encode("RO");
        assertEquals("R000", encoded5);
        
        String encoded6 = soundex.encode("ROB");
        assertEquals("R100", encoded6);
        
        // Error scenarios
        try {
            soundex.encode(null);
            fail("Exception not thrown for null input");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
                
    }
    
    @Test
    public void testSoundex_CustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        Soundex soundex = new Soundex(customMapping);
        
        // Typical use cases
        String encoded1 = soundex.encode("Robert");
        assertEquals("R54874", encoded1);
        
        String encoded2 = soundex.encode("Rupert");
        assertEquals("R54874", encoded2);
        
        String encoded3 = soundex.encode("Rubin");
        assertEquals("R0467", encoded3);
        
        // Edge cases
        String encoded4 = soundex.encode("R");
        assertEquals("R0000", encoded4);
        
        String encoded5 = soundex.encode("RO");
        assertEquals("R0000", encoded5);
        
        String encoded6 = soundex.encode("ROB");
        assertEquals("R1000", encoded6);
        
        // Error scenarios
        try {
            soundex.encode(null);
            fail("Exception not thrown for null input");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testEncode_ObjectType() {
        Soundex soundex = new Soundex();
        
        // Typical use case
        Object encodedObject = soundex.encode("Robert");
        assertEquals("R163", encodedObject);
        
        // Error scenarios
        try {
            soundex.encode(123);
            fail("Exception not thrown for non-string object");
        } catch (EncoderException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        
        // Typical use cases
        int difference1 = soundex.difference("Robert", "Rupert");
        assertEquals(4, difference1);
        
        int difference2 = soundex.difference("Rubin", "Rupert");
        assertEquals(3, difference2);
        
        int difference3 = soundex.difference("Robert", "Alfred");
        assertEquals(0, difference3);
        
        // Edge cases
        int difference4 = soundex.difference("R", "R");
        assertEquals(4, difference4);
        
        int difference5 = soundex.difference("RO", "R");
        assertEquals(4, difference5);
        
        int difference6 = soundex.difference("ROB", "R");
        assertEquals(4, difference6);
        
        // Error scenarios
        try {
            soundex.difference(null, "Robert");
            fail("Exception not thrown for null input");
        } catch (EncoderException e) {
            // Exception expected
        }
        
        try {
            soundex.difference("Robert", null);
            fail("Exception not thrown for null input");
        } catch (EncoderException e) {
            // Exception expected
        }
                
    }

    @Test
    public void testSoundex_CustomMappingString() {
        String customMapping = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Soundex soundex = new Soundex(customMapping);
        
        // Typical use cases
        String encoded1 = soundex.encode("Robert");
        assertEquals("R163", encoded1);
        
        String encoded2 = soundex.encode("Rupert");
        assertEquals("R163", encoded2);
        
        String encoded3 = soundex.encode("Rubin");
        assertEquals("R150", encoded3);
        
        // Edge cases
        String encoded4 = soundex.encode("R");
        assertEquals("R000", encoded4);
        
        String encoded5 = soundex.encode("RO");
        assertEquals("R000", encoded5);
        
        String encoded6 = soundex.encode("ROB");
        assertEquals("R100", encoded6);
        
        // Error scenarios
        try {
            soundex.encode(null);
            fail("Exception not thrown for null input");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
                
    }
}