import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    
    Soundex soundex = new Soundex();

    @Test
    public void testEncode() {
        assertEquals("S532", soundex.encode("Soundex"));
    }

    @Test
    public void testEncodeEmptyString() {
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncodeNullString() {
        assertNull(soundex.encode(null));
    }

    @Test
    public void testEncodeWithNumbers() {
        assertEquals("S300", soundex.encode("S0und3x"));
    }

    @Test
    public void testEncodeWithSpecialCharacters() {
        assertEquals("S532", soundex.encode("Soundex@#$!"));
    }

    @Test
    public void testEncodeWithLowerCase() {
        assertEquals("S532", soundex.encode("soundex"));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, soundex.difference("Soundex", "Soundex"));
    }

    @Test
    public void testDifferenceTwoDifferentWords() throws EncoderException {
        assertEquals(0, soundex.difference("Soundex", "Difference"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullValues() throws EncoderException {
        soundex.difference(null, null);
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        assertEquals(0, soundex.difference("", ""));
    }
}