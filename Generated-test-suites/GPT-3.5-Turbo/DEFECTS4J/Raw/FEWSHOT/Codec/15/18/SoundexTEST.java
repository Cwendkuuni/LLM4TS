import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    Soundex soundex = new Soundex();

    @Test
    public void testEncode() {
        assertEquals("L452", soundex.encode("Lincoln"));
        assertEquals("S532", soundex.encode("Smith"));
        assertEquals("T522", soundex.encode("Thompson"));
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(0, soundex.difference("Lincoln", "Smith"));
        assertEquals(1, soundex.difference("Smith", "Smyth"));
        assertEquals(4, soundex.difference("Thompson", "Tompson"));
        assertEquals(2, soundex.difference("Tompson", "Tumpsin"));
    }
}