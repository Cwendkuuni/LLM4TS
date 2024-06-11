import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals(caverphone.caverphone("hello"), "HL11111111");
        
        // Edge case - empty string
        assertEquals(caverphone.caverphone(""), "1111111111");
        
        // Edge case - string with special characters
        assertEquals(caverphone.caverphone("!@#123"), "");

    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals(caverphone.encode("hello"), "HL11111111");
        
        // Error scenario - Object is not a String
        try {
            caverphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals(caverphone.encode("hello"), "HL11111111");
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case - same caverphone codes
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        
        // Typical use case - different caverphone codes
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }
}