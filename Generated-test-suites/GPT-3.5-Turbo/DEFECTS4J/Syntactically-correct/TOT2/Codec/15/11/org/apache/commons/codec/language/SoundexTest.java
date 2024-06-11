import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testSoundexEncode() {
        assertEquals("A261", soundex.encode("Anthony"));
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        int difference = soundex.difference("John", "Joan");
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex customSoundex = new Soundex(customMapping);
        assertEquals("3333", customSoundex.encode("AEIOU"));
    }

    @Test
    public void testSoundexInvalidCharacter() {
        try {
            soundex.encode("123");
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testSoundexEmptyString() {
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testSoundexMaxLength() {
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSoundexUSMapping() {
        Soundex usEnglishSoundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertEquals("A261", usEnglishSoundex.encode("Anthony"));
    }

}