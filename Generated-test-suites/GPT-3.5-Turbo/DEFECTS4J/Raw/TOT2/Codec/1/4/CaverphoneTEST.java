import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Edge case
        assertEquals("B11111111", caverphone.caverphone("be")); // Testing removal of final e
        
        // Error scenario
        assertEquals("1111111111", caverphone.caverphone(null));
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.encode("")); 
        
        // Edge case
        assertEquals("B11111111", caverphone.encode("be")); // Testing removal of final e
        
        // Error scenario
        try {
            caverphone.encode(123); // Should throw EncoderException
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.encode("")); 
        
        // Edge case
        assertEquals("B11111111", caverphone.encode("be")); // Testing removal of final e
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertTrue(caverphone.isCaverphoneEqual("hello", "helo"));
        
        // Edge case
        assertFalse(caverphone.isCaverphoneEqual("hello", "different"));
    }
}