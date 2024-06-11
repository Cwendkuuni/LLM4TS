import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.Soundex;

import static org.junit.Assert.assertEquals;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        String result = soundex.encode("Test");
        assertEquals("T230", result);
    }

    @Test
    public void testEncodeWithEmptyString() {
        String result = soundex.encode("");
        assertEquals("", result);
    }

    @Test
    public void testEncodeWithNullString() {
        String result = soundex.encode(null);
        assertEquals(null, result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        int result = soundex.difference("Test", "Taste");
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        int result = soundex.difference("", "");
        assertEquals(0, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonStringObject() throws EncoderException {
        soundex.difference(123, "Taste");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceWithInvalidCharacters() throws EncoderException {
        soundex.difference("Test!", "Taste");
    }

    @Test
    public void testSoundex() {
        String result = soundex.soundex("Test");
        assertEquals("T230", result);
    }

    @Test
    public void testSoundexWithEmptyString() {
        String result = soundex.soundex("");
        assertEquals("", result);
    }

    @Test
    public void testSoundexWithNullString() {
        String result = soundex.soundex(null);
        assertEquals(null, result);
    }

    @Test
    public void testSoundexWithMapping() {
        String result = new Soundex("00000000000002220334455012623010203239").soundex("Test");
        assertEquals("T418", result);
    }

}