import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S053", soundex.encode("Soundex"));
        assertEquals("R150", soundex.encode("Robert"));
        assertEquals("H455", soundex.encode("Hello"));
    }

    @Test
    public void testEncodeWithNull() {
        Soundex soundex = new Soundex();
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Soundex", "soundex"));
        assertEquals(2, soundex.difference("Robert", "Robin"));
        assertEquals(3, soundex.difference("Hello", "Hall"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonString() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(123, "Soundex");
    }
}