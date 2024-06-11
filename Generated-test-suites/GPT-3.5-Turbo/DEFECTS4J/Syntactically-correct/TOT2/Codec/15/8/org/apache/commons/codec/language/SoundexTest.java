import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    Soundex soundex = new Soundex();

    @Test
    public void testSoundexEncodeString() {
        assertEquals("S532", soundex.encode("Soundex"));
    }

    @Test
    public void testSoundexEncodeObject() {
        try {
            assertEquals("T200", (String) soundex.encode((Object) "Test"));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSoundexDifference() {
        try {
            assertEquals(3, soundex.difference("Sound", "Test"));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSoundexDifferenceWithEmptyStrings() {
        try {
            assertEquals(0, soundex.difference("", ""));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSoundexDifferenceWithNullStrings() {
        try {
            assertEquals(0, soundex.difference(null, null));
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCustomMapping() {
        Soundex customSoundex = new Soundex("04440004224550112623650302");
        assertEquals("H244", customSoundex.encode("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapException() {
        soundex.soundex("123"); // Should throw IllegalArgumentException
    }

}