import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean_nullString_returnNull() {
        String str = null;
        String expectedResult = null;
        
        String result = SoundexUtils.clean(str);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testClean_emptyString_returnEmptyString() {
        String str = "";
        String expectedResult = "";
        
        String result = SoundexUtils.clean(str);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testClean_nonAlphabeticCharacters_returnOnlyUpperCaseLetters() {
        String str = "1A!bC@3";
        String expectedResult = "ABC";
        
        String result = SoundexUtils.clean(str);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testClean_upperCaseLetters_returnUpperCaseLetters() {
        String str = "ABC";
        String expectedResult = "ABC";
        
        String result = SoundexUtils.clean(str);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testClean_mixedCaseLetters_returnUpperCaseLetters() {
        String str = "aBc";
        String expectedResult = "ABC";
        
        String result = SoundexUtils.clean(str);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDifference_bothEncodedStringsNull_returnZero() {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = null;
        int expectedResult = 0;
        
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            assertEquals(expectedResult, result);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDifference_oneEncodedStringNull_returnZero() {
        StringEncoder encoder = new Soundex();
        String s1 = "ABC";
        String s2 = null;
        int expectedResult = 0;
        
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            assertEquals(expectedResult, result);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDifference_bothEncodedStrings_returnDifference() {
        StringEncoder encoder = new Soundex();
        String s1 = "ABC";
        String s2 = "DEF";
        int expectedResult = 0;
        
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            assertEquals(expectedResult, result);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDifferenceEncoded_bothEncodedStringsNull_returnZero() {
        String es1 = null;
        String es2 = null;
        int expectedResult = 0;
        
        int result = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDifferenceEncoded_oneEncodedStringNull_returnZero() {
        String es1 = "123";
        String es2 = null;
        int expectedResult = 0;
        
        int result = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDifferenceEncoded_bothEncodedStrings_returnDifference() {
        String es1 = "123";
        String es2 = "456";
        int expectedResult = 0;
        
        int result = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(expectedResult, result);
    }
}