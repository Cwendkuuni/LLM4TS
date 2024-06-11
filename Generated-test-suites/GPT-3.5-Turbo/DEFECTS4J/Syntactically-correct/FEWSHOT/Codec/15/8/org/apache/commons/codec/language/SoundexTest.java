import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("N231", soundex.encode("Nolan"));
        assertEquals("S516", soundex.encode("Smith"));
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Nolan", "Nolan"));
        assertEquals(0, soundex.difference("Nolan", "Smith"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("N400", soundex.soundex("Nolan"));
        assertEquals("S530", soundex.soundex("Smith"));
        assertNull(soundex.soundex(null));
    }
}