import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        // typical use cases
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        assertEquals("FX", metaphone.metaphone("Fox"));
        assertEquals("X", metaphone.metaphone("Scratch"));
        assertEquals("SRTX", metaphone.metaphone("StringEncoder"));
        
        // edge cases
        assertEquals("T", metaphone.metaphone("T"));
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("", metaphone.metaphone(""));
        
        // error scenarios
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testEncode() throws EncoderException {
        // typical use cases
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        assertEquals("FX", metaphone.encode("Fox"));
        assertEquals("X", metaphone.encode("Scratch"));
        assertEquals("SRTX", metaphone.encode("StringEncoder"));
        
        // edge cases
        assertEquals("T", metaphone.encode("T"));
        assertEquals("A", metaphone.encode("A"));
        assertEquals("", metaphone.encode(""));
        
        // error scenarios
        try {
            metaphone.encode(new Object());
            assertTrue("Did not throw EncoderException for invalid input", false);
        } catch (EncoderException e) {
            assertTrue("Threw EncoderException for invalid input", true);
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        // typical use cases
        assertTrue(metaphone.isMetaphoneEqual("Metaphone", "Metafone"));
        assertTrue(metaphone.isMetaphoneEqual("Dog", "Dogs"));
        assertTrue(metaphone.isMetaphoneEqual("Jump", "Jimp"));
        assertTrue(metaphone.isMetaphoneEqual("Dance", "Dans"));
        
        // edge cases
        assertTrue(metaphone.isMetaphoneEqual("", ""));
        assertTrue(metaphone.isMetaphoneEqual("T", "A"));
        assertTrue(metaphone.isMetaphoneEqual("A", ""));
        
        // error scenarios
        assertTrue(!metaphone.isMetaphoneEqual("", "Metafone"));
    }
}