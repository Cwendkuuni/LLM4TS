
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_ObjectOfTypeString_ReturnsEncodedString() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("S532", soundex.encode("Smith"));
        assertEquals("T400", soundex.encode("Taylor"));
        assertEquals("A261", soundex.encode("Adams"));
    }

    @Test
    public void testEncode_ObjectNotOfTypeString_ThrowsEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        assertThrows(EncoderException.class, () -> {
            soundex.encode(123);
        });
    }

    @Test
    public void testEncode_EmptyString_ReturnsEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testDifference_TwoDifferentStrings_ReturnsNumberOfSameCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Smith", "Smith"));
        assertEquals(3, soundex.difference("Smith", "Taylor"));
        assertEquals(2, soundex.difference("Smith", "Adams"));
        assertEquals(0, soundex.difference("Smith", "Jones"));
    }

    @Test
    public void testDifference_OneStringIsNull_ThrowsEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        assertThrows(EncoderException.class, () -> {
           soundex.difference("Smith", null);
        });
    }

    @Test
    public void testSoundex_NormalString_ReturnsCorrespondingSoundexCode() {
        Soundex soundex = new Soundex();
        assertEquals("S532", soundex.soundex("Smith"));
        assertEquals("T460", soundex.soundex("Taylor"));
        assertEquals("A352", soundex.soundex("Adams"));
    }

    @Test
    public void testSoundex_EmptyString_ReturnsEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void testSoundex_StringWithNonMappedCharacters_ThrowsIllegalArgumentException() {
        Soundex soundex = new Soundex();
        assertThrows(IllegalArgumentException.class, () -> {
           soundex.soundex("123");
        });
    }
}

