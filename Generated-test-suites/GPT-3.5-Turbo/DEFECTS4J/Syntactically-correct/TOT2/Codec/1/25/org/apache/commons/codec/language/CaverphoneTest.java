import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone c = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", c.caverphone(null));
        assertEquals("1111111111", c.caverphone(""));
        assertEquals("KF", c.caverphone("cough"));
        
        // Edge cases
        assertEquals("A3223", c.caverphone("aeiou"));
        assertEquals("A3223", c.caverphone("aeiouaeiou"));
        
        // Error scenarios
        assertEquals("1111111111", c.caverphone("12345"));      
    }

    @Test
    public void testEncode_String() {
        Caverphone c = new Caverphone();
        
        // Typical use case
        assertEquals("KF", c.encode("cough"));
        
        // Edge cases
        assertEquals("A3223", c.encode("aeiou"));
        
        // Error scenarios
        try {
            c.encode(123);
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_Object() {
        Caverphone c = new Caverphone();
        
        // Typical use case
        assertEquals("KF", c.encode((Object) "cough"));
        
        // Error scenarios
        try {
            c.encode(123);
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone c = new Caverphone();
        
        // Typical use case
        assertTrue(c.isCaverphoneEqual("cough", "cough"));
        assertFalse(c.isCaverphoneEqual("cough", "tough"));
        
        // Edge cases
        assertFalse(c.isCaverphoneEqual("aeiou", "abcdef"));
    }
}