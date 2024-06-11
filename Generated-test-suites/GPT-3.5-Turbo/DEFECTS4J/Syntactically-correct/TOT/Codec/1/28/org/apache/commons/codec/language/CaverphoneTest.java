import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    // Test caverphone method
    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("A11111111A", caverphone.caverphone("Apache"));
        
        // Edge case: empty string
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Error scenario: null input
        assertEquals("1111111111", caverphone.caverphone(null));
    }
    
    // Test encode Object method
    @Test
    public void testEncode_Object() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        try {
            Object result = caverphone.encode("Apache");
            assertEquals("A11111111A", result);
        } catch (EncoderException e) {
            fail("Unexpected EncoderException");
        }
        
        // Error scenario: invalid input type
        try {
            Object result = caverphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Exception expected
        }
    }
    
    // Test encode String method
    @Test
    public void testEncode_String() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        String result = caverphone.encode("Apache");
        assertEquals("A11111111A", result);
        
        // Edge case: empty string
        result = caverphone.encode("");
        assertEquals("1111111111", result);
    }
    
    // Test isCaverphoneEqual method
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case: matching caverphones
        boolean result = caverphone.isCaverphoneEqual("Apache", "Apache");
        assertTrue(result);
        
        // Typical use case: non-matching caverphones
        result = caverphone.isCaverphoneEqual("Apache", "Apocalpse");
        assertFalse(result);
    }

}