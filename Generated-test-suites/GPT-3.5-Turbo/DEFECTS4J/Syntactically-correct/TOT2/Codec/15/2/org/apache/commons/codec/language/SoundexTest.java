import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SoundexTest {

    @Test
    public void testSoundexEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Hello");
        assertEquals("H400", result);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Hello", "Hall");
        assertEquals(3, result);
    }

    @Test
    public void testSoundexDifferenceSameStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Hello", "Hello");
        assertEquals(4, result);
    }

    @Test(expected = EncoderException.class)
    public void testSoundexDifferenceNonStringParameter() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(123, "Hello");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexInvalidCharacterMapping() {
        Soundex soundex = new Soundex();
        soundex.soundex("Hello@");
    }
}