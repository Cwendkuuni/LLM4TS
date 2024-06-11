import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    SoundexUtils soundexUtils;
    
    @Before
    public void setup() {
        soundexUtils = new SoundexUtils();
    }
    
    @Test
    public void testClean_NullString_ReturnsNull() {
        String str = null;
        String cleanStr = SoundexUtils.clean(str);
        assertEquals(null, cleanStr);
    }
    
    @Test
    public void testClean_EmptyString_ReturnsEmptyString() {
        String str = "";
        String cleanStr = SoundexUtils.clean(str);
        assertEquals("", cleanStr);
    }
    
    @Test
    public void testClean_UpperCaseLettersOnly_ReturnsSameString() {
        String str = "HELLO";
        String cleanStr = SoundexUtils.clean(str);
        assertEquals("HELLO", cleanStr);
    }
    
    @Test
    public void testClean_MixedCaseLetters_ReturnsUpperCaseOnly() {
        String str = "HelloWoRld";
        String cleanStr = SoundexUtils.clean(str);
        assertEquals("HELLOWORLD", cleanStr);
    }
    
    @Test
    public void testClean_SpecialCharacters_ReturnsUpperCaseOnly() {
        String str = "!@#$%^&*()";
        String cleanStr = SoundexUtils.clean(str);
        assertEquals("", cleanStr);
    }
    
    @Test
    public void testDifference_EncodedStringsDifferByOneCharacter_ReturnsOne() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "ABCD";
        String s2 = "ABCE";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(1, difference);
    }
    
    @Test
    public void testDifference_EncodedStringsDifferByTwoCharacters_ReturnsTwo() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "ABCD";
        String s2 = "ACDE";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, difference);
    }
    
    @Test
    public void testDifference_EncodedStringsDifferByThreeCharacters_ReturnsThree() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "ABCD";
        String s2 = "BCDE";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(3, difference);
    }
    
    @Test
    public void testDifference_EncodedStringsDifferByFourCharacters_ReturnsFour() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "ABCD";
        String s2 = "CDEF";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, difference);
    }
    
    @Test
    public void testDifference_EncodedStringsDifferByMoreThanFourCharacters_ReturnsFive() throws EncoderException {
        StringEncoder encoder = new SomeEncoder();
        String s1 = "ABCD";
        String s2 = "ZXYZ";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(5, difference);
    }
    
}

class SomeEncoder implements StringEncoder {
    
    public String encode(String str) throws EncoderException {
        return str;
    }
    
}