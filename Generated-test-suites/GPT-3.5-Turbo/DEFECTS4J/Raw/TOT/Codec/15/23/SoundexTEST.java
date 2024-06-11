import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode_ValidInput_ReturnsSoundexCode() {
        String soundexCode = soundex.encode("Robert");
        assertEquals("R163", soundexCode);
    }

    @Test
    public void testEncode_NullInput_ReturnsNull() {
        String soundexCode = soundex.encode(null);
        assertNull(soundexCode);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_InvalidInput_ThrowsEncoderException() throws EncoderException {
        soundex.encode(123);
    }

    @Test
    public void testDifference_SimilarStrings_ReturnsFullScore() throws EncoderException {
        int difference = soundex.difference("Robert", "Rupert");
        assertEquals(4, difference);
    }

    @Test
    public void testDifference_DifferentStrings_ReturnsZeroScore() throws EncoderException {
        int difference = soundex.difference("Abcd", "Efgh");
        assertEquals(0, difference);
    }

    @Test
    public void testDifference_EmptyStrings_ReturnsZeroScore() throws EncoderException {
        int difference = soundex.difference("", "");
        assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_InvalidInput_ThrowsEncoderException() throws EncoderException {
        soundex.difference("abc", 123);
    }
}