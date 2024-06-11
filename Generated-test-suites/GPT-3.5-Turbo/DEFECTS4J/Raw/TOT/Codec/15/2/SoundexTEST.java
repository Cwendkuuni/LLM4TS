import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_NullInput_ReturnNull() {
        Soundex soundex = new Soundex();
        assertNull(soundex.encode(null));
    }

    @Test
    public void testEncode_EmptyString_ReturnEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncode_SimilarInputs_ReturnSameSoundexCode() {
        Soundex soundex = new Soundex();
        assertEquals(soundex.encode("Robert"), soundex.encode("Rupert"));
    }

    @Test
    public void testEncode_DifferentInputs_ReturnDifferentSoundexCode() {
        Soundex soundex = new Soundex();
        assertNotEquals(soundex.encode("Robert"), soundex.encode("Dolores"));
    }

    @Test(expected = EncoderException.class)
    public void testEncode_NonStringInput_ThrowEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(12345);
    }

    @Test
    public void testDifference_NullInputs_ReturnZero() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference(null, null));
    }

    @Test
    public void testDifference_EmptyInputs_ReturnZero() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("", ""));
    }

    @Test
    public void testDifference_SimilarInputs_ReturnFour() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Robert", "Rupert"));
    }

    @Test
    public void testDifference_DifferentInputs_ReturnNonZero() throws EncoderException {
        Soundex soundex = new Soundex();
        assertNotEquals(0, soundex.difference("Robert", "Dolores"));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_NonStringInputs_ThrowEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(12345, "Test");
    }

    @Test
    public void testMap_ValidChar_ReturnMappedChar() {
        Soundex soundex = new Soundex();
        assertEquals('0', soundex.map('A'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMap_InvalidChar_ThrowIllegalArgumentException() {
        Soundex soundex = new Soundex();
        soundex.map('!');
    }
}