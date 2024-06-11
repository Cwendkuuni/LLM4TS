import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        assertEquals("K3T", caverphone.caverphone("cat"));
        assertEquals("KR3T", caverphone.caverphone("cart"));
        
        // Potential edge cases and error scenarios
        // Testing with special characters
        assertEquals("KR3T", caverphone.caverphone("c@rt"));
        
        // Testing with numbers
        assertEquals("Y111111111", caverphone.caverphone("123"));
        
        // Testing with different locales
        assertEquals("K3T", caverphone.caverphone("cAt"));

        // Testing with very long input
        String longString = "a".repeat(1000);
        assertNotNull(caverphone.caverphone(longString));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        
        assertEquals("K3T", caverphone.encode("cat"));
        assertEquals("KR3T", caverphone.encode("cart"));
        
        // Testing error scenario where input is not a String
        try {
            caverphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        assertTrue(caverphone.isCaverphoneEqual("cat", "cat"));
        assertFalse(caverphone.isCaverphoneEqual("cat", "dog"));
        
        // Testing with special characters
        assertTrue(caverphone.isCaverphoneEqual("cat!", "ca@t"));
        
        // Testing with different cases
        assertTrue(caverphone.isCaverphoneEqual("CAT", "cat"));
        
        // Testing with edge cases
        assertTrue(caverphone.isCaverphoneEqual("!", "!"));
    }
}