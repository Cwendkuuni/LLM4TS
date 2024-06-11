import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        // Typical Use Case
        assertEquals("1111111111", caverphone.caverphone(null));
        
        // Edge Case
        assertEquals("1111111111", caverphone.caverphone(""));
        
        // Typical Use Case
        assertEquals("km2ssssn", caverphone.caverphone("Communities"));
        
        // Edge Case
        assertEquals("n2kndn2n", caverphone.caverphone("discussion"));
        
        // Typical Use Case
        assertEquals("weak1", caverphone.caverphone("weekend"));
    }

    @Test
    public void testEncodeObject() {
        // Typical Use Case
        try {
            assertEquals("t2ch", (String) caverphone.encode((Object) "touch"));
        } catch (EncoderException e) {
            fail("Unexpected exception");
        }
        
        // Error Scenario
        try {
            caverphone.encode(new Integer(5));
            fail("Expected exception");
        } catch (EncoderException e) {
            // exception expected
        }
    }

    @Test
    public void testEncodeString() {
        // Typical Use Case
        assertEquals("s1r", caverphone.encode("sir"));
        
        // Edge Case
        assertEquals("J23", caverphone.encode("joy"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        // Typical Use Case
        assertTrue(caverphone.isCaverphoneEqual("mouse", "mouss"));
        
        // Typical Use Case
        assertFalse(caverphone.isCaverphoneEqual("mouse", "house"));
        
        // Edge Case
        assertTrue(caverphone.isCaverphoneEqual("", ""));
    }
}