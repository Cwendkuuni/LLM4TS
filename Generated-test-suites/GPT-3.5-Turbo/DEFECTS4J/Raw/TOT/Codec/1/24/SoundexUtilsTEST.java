import org.apache.commons.codec.EncoderException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {
    
    private Soundex soundex;
    private RefinedSoundex refinedSoundex;
    
    @Before
    public void setUp() {
        soundex = new Soundex();
        refinedSoundex = new RefinedSoundex();
    }
    
    @Test
    public void testClean() {
        String str = "abc123";
        String expected = "ABC";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClean_nullString() {
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testClean_emptyString() {
        String str = "";
        String expected = "";
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        String s1 = "hello";
        String s2 = "hallo";
        int expected = 3;
        int actual = SoundexUtils.difference(soundex, s1, s2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDifference_encodedStringsNull() throws EncoderException {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDifference_encodedStringsDifferentLength() throws EncoderException {
        String es1 = "ABCAA";
        String es2 = "ABCAAA";
        int expected = 5;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }
    
    @Test(expected = EncoderException.class)
    public void testDifference_encoderException() throws EncoderException {
        String s1 = "hello";
        String s2 = "hallo";
        
        // Assuming the encoder throws an exception for invalid input
        SoundexUtils.difference(null, s1, s2);
    }
}