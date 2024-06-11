import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_NormalInput_ReturnsCorrectSoundexCode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Robert");
        assertEquals("R163", encoded);
    }
    
    @Test
    public void testEncode_EmptyInput_ReturnsEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("");
        assertEquals("", encoded);
    }
    
    @Test
    public void testEncode_NullInput_ReturnsNull() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode(null);
        assertNull(encoded);
    }
    
    @Test
    public void testEncode_InputWithSpecialCharacters_ReturnsSoundexCodeIgnoringSpecialCharacters() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("John-Doe");
        assertEquals("J530", encoded);
    }
    
    @Test
    public void testDifference_SimilarStrings_ReturnsFour() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Doe", "Dough");
        assertEquals(4, difference);
    }
    
    @Test
    public void testDifference_DissimilarStrings_ReturnsZero() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Smith", "Johnson");
        assertEquals(0, difference);
    }
    
    @Test(expected = EncoderException.class)
    public void testDifference_InvalidInput_ThrowsEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference("Name", 123);
    }
    
    @Test
    public void testSoundex_NormalInput_ReturnsCorrectSoundexCode() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("Johnson");
        assertEquals("J525", soundexCode);
    }
    
    @Test
    public void testSoundex_EmptyInput_ReturnsEmptyString() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("");
        assertEquals("", soundexCode);
    }
    
    @Test
    public void testSoundex_NullInput_ReturnsNull() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex(null);
        assertNull(soundexCode);
    }
    
    @Test
    public void testSoundex_InputWithSpecialCharacters_ReturnsSoundexCodeIgnoringSpecialCharacters() {
        Soundex soundex = new Soundex();
        String soundexCode = soundex.soundex("Jo@hn-D0e");
        assertEquals("J530", soundexCode);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMap_CharacterNotMapped_ThrowsIllegalArgumentException() {
        Soundex soundex = new Soundex();
        soundex.map('Z');
    }
    
    @Test
    public void testSoundex_NonEnglishCharacters_ReturnsSoundexCode() {
        Soundex soundex = new Soundex("0123Ç01ÛDVD");
        String soundexCode = soundex.soundex("ÇÛH");
        assertEquals("012", soundexCode);
    }
}