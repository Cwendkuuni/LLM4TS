import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A260", soundex.soundex("Apache"));
        assertEquals("Z000", soundex.soundex("Zebra"));
        assertNull(soundex.soundex(null));
        assertEquals("", soundex.soundex(""));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A260", soundex.encode("Apache"));
        assertEquals("Z000", soundex.encode("Zebra"));
        try {
            soundex.encode(123);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Apache", "Zebra"));
        assertEquals(4, soundex.difference("Apache", "A260"));
        assertEquals(4, soundex.difference("A260", "A260"));
        try {
            soundex.difference(null, "Apache");
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
        try {
            soundex.difference("Apache", null);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
}