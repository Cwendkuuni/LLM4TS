import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Edge cases
        assertEquals("3", caverphone.caverphone("a"));
        assertEquals("1111111111", caverphone.caverphone("123"));
        assertEquals("2k2n2", caverphone.caverphone("a1b2c3"));
        
        // Exception handling
        try {
            caverphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        
        // Edge cases
        assertEquals("3", caverphone.encode("a"));
        assertEquals("1111111111", caverphone.encode("123"));
        assertEquals("2k2n2", caverphone.encode("a1b2c3"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use case
        assertTrue(caverphone.isCaverphoneEqual("string1", "string1"));
        assertFalse(caverphone.isCaverphoneEqual("string1", "string2"));
        
        // Edge cases
        assertTrue(caverphone.isCaverphoneEqual("a", "a"));
        assertFalse(caverphone.isCaverphoneEqual("a", "b"));
    }

}