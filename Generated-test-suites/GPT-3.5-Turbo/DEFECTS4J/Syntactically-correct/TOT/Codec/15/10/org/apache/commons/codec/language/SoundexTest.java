import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    /*
     * Test cases for encode(Object obj)
     */

    @Test
    public void testEncode_ValidString_ReturnsSoundexCode() {
        assertEquals("A261", soundex.encode("Apache"));
    }

    @Test(expected = EncoderException.class)
    public void testEncode_ObjectNotString_ThrowsEncoderException() throws EncoderException {
        soundex.encode(12345);
    }

    /*
     * Test cases for encode(String str)
     */

    @Test
    public void testEncode_ValidString_ReturnsSoundexCode() {
        assertEquals("A261", soundex.encode("Apache"));
    }

    @Test
    public void testEncode_NullString_ReturnsNull() {
        assertNull(soundex.encode(null));
    }

    /*
     * Test cases for difference(String s1, String s2)
     */

    @Test
    public void testDifference_EqualStrings_ReturnsFour() throws EncoderException {
        assertEquals(4, soundex.difference("Apache", "Apache"));
    }

    @Test
    public void testDifference_DifferentStrings_ReturnsZero() throws EncoderException {
        assertEquals(0, soundex.difference("Apache", "Eclipse"));
    }

    /*
     * Test cases for soundex(String str)
     */

    @Test
    public void testSoundex_ValidString_ReturnsSoundexCode() {
        assertEquals("A261", soundex.soundex("Apache"));
    }

    @Test
    public void testSoundex_NullString_ReturnsNull() {
        assertNull(soundex.soundex(null));
    }

    @Test
    public void testSoundex_EmptyString_ReturnsEmptyString() {
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void testSoundex_StringWithMultipleSpaces_ReturnsSoundexCodeWithoutSpaces() {
        assertEquals("A261", soundex.soundex("A   p  a   c  he"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundex_StringWithInvalidCharacter_ThrowsIllegalArgumentException() {
        soundex.soundex("Apache$");
    }

}