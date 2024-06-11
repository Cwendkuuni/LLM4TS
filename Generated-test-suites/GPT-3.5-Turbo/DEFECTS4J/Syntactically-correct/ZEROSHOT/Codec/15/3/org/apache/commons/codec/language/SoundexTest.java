import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        String encoded = soundex.encode("Apache");
        assertEquals("A120", encoded);
    }

    @Test
    public void testEncodeWithEmptyString() {
        String encoded = soundex.encode("");
        assertEquals("", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        int difference = soundex.difference("Apache", "Apeche");
        assertEquals(3, difference);
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        int difference = soundex.difference("", "");
        assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonStringObjects() throws EncoderException {
        soundex.difference(123, "Apeche");
    }

    @Test
    public void testSoundex() {
        String soundexCode = soundex.soundex("Apache");
        assertEquals("A120", soundexCode);
    }

    @Test
    public void testSoundexWithEmptyString() {
        String soundexCode = soundex.soundex("");
        assertEquals("", soundexCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexWithInvalidCharacter() {
        soundex.soundex("Ap#che");
    }
}