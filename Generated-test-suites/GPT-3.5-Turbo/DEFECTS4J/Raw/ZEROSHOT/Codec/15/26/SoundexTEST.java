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
    public void difference_WhenTwoEqualStrings_ThenReturnFour() throws EncoderException {
        int result = soundex.difference("hello", "hello");
        assertEquals(4, result);
    }
    
    @Test
    public void difference_WhenTwoDifferentStrings_ThenReturnZero() throws EncoderException {
        int result = soundex.difference("hello", "world");
        assertEquals(0, result);
    }
    
    @Test
    public void difference_WhenStringsWithDifferentCases_ThenReturnFour() throws EncoderException {
        int result = soundex.difference("HeLlO", "hello");
        assertEquals(4, result);
    }
    
    @Test
    public void encode_WhenObjectIsString_ThenReturnEncodedString() throws EncoderException {
        Object result = soundex.encode("hello");
        assertEquals("H400", result);
    }
    
    @Test(expected = EncoderException.class)
    public void encode_WhenObjectIsNotString_ThenThrowEncoderException() throws EncoderException {
        soundex.encode(123);
    }
    
    @Test
    public void encode_WhenStringIsValid_ThenReturnEncodedString() {
        String result = soundex.encode("hello");
        assertEquals("H400", result);
    }
    
    @Test
    public void encode_WhenStringIsEmpty_ThenReturnEmptyString() {
        String result = soundex.encode("");
        assertEquals("", result);
    }
    
    @Test
    public void encode_WhenStringIsNull_ThenReturnNull() {
        String result = soundex.encode(null);
        assertNull(result);
    }
    
    @Test
    public void soundex_WhenStringIsValid_ThenReturnSoundexCode() {
        String result = soundex.soundex("hello");
        assertEquals("H400", result);
    }
    
    @Test
    public void soundex_WhenStringIsEmpty_ThenReturnEmptyString() {
        String result = soundex.soundex("");
        assertEquals("", result);
    }
    
    @Test
    public void soundex_WhenStringIsNull_ThenReturnNull() {
        String result = soundex.soundex(null);
        assertNull(result);
    }
}