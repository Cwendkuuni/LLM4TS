import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_NullStringShouldReturnNull() {
        Soundex soundex = new Soundex();
        assertNull(soundex.encode(null));
    }

    @Test
    public void testEncode_EmptyStringShouldReturnEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncode_SingleCharacterStringShouldReturnSameCharacter() {
        Soundex soundex = new Soundex();
        assertEquals("A", soundex.encode("A"));
    }

    @Test
    public void testEncode_StringWithAllMappedCharactersShouldReturnCorrectCode() {
        Soundex soundex = new Soundex();
        assertEquals("0123", soundex.encode("ABCD"));
    }

    @Test
    public void testEncode_StringWithInvalidCharacterShouldThrowException() {
        Soundex soundex = new Soundex();
        try {
            soundex.encode("ABCDE!");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The character is not mapped: !", e.getMessage());
        }
    }

    @Test
    public void testEncode_StringWithMappedAndUnmappedCharactersShouldReturnCorrectCode() {
        Soundex soundex = new Soundex();
        assertEquals("012A", soundex.encode("ABCD!"));
    }

    @Test
    public void testDifference_SameStringsShouldReturnFour() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("ABCD", "ABCD"));
    }

    @Test
    public void testDifference_DifferentStringsShouldReturnZero() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("ABCD", "EFGH"));
    }

    @Test
    public void testDifference_SimilarStringsShouldReturnCorrectValue() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(3, soundex.difference("Helloworld", "Halloworld"));
    }
    
}