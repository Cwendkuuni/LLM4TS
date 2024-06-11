import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("kftn", caverphone.caverphone("CoughRaNGhTough123"));
        assertEquals("fgkks", caverphone.caverphone("frE4k1n PurpLe c00kieS"));
        
        // Edge cases
        assertEquals("A", caverphone.caverphone("a"));
        assertEquals("3", caverphone.caverphone("e"));
        assertEquals("2", caverphone.caverphone("h"));
        assertEquals("3", caverphone.caverphone("l"));
        assertEquals("2", caverphone.caverphone("r"));
        assertEquals("A", caverphone.caverphone("y"));
        assertEquals("AAAAAAA1111", caverphone.caverphone("aaaaaaaaaEEEEEEEEEEEEEEkkkkkkkkkkkkkkFFFFFFFFFFFFFFFFFFFfMMnmnMNWnwNW"));
        assertEquals("TRFMMRSMM24", caverphone.caverphone("TtRRRRRfffffMMMMMMMMMM555525524"));
        
        // Error scenarios
        assertNull(caverphone.caverphone(null));
    }
    
    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("kftn", caverphone.encode("CoughRaNGhTough123"));
        assertEquals("fgkks", caverphone.encode("frE4k1n PurpLe C00kieS"));
        
        // Error scenarios
        try {
            caverphone.encode(123); // Invalid input, should throw EncoderException
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Exception expected, do nothing
        }
    }
    
    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        
        // Typical use cases
        assertTrue(caverphone.isCaverphoneEqual("", null));
        assertTrue(caverphone.isCaverphoneEqual("c@t", "kat"));
        assertFalse(caverphone.isCaverphoneEqual("D0g", "cat"));
        
        // Error scenarios
        try {
            caverphone.isCaverphoneEqual(123, 456); // Invalid inputs, should throw EncoderException
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Exception expected, do nothing
        }
    }
}