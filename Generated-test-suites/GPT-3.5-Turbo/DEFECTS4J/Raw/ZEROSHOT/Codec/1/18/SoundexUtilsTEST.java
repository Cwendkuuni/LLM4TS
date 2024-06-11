import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanWithNullString() {
        String str = null;
        String expected = null;
        
        String cleaned = SoundexUtils.clean(str);
        
        assertEquals(expected, cleaned);
    }
    
    @Test
    public void testCleanWithEmptyString() {
        String str = "";
        String expected = "";
        
        String cleaned = SoundexUtils.clean(str);
        
        assertEquals(expected, cleaned);
    }
    
    @Test
    public void testCleanWithNonAlphabeticCharacters() {
        String str = "123*!@#";
        String expected = "";
        
        String cleaned = SoundexUtils.clean(str);
        
        assertEquals(expected, cleaned);
    }
    
    @Test
    public void testCleanWithMixedCaseLetters() {
        String str = "aBcDeFgHiJ";
        String expected = "ABCDEFGHIJ";
        
        String cleaned = SoundexUtils.clean(str);
        
        assertEquals(expected, cleaned);
    }
    
    @Test
    public void testCleanWithUpperCaseLetters() {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        String cleaned = SoundexUtils.clean(str);
        
        assertEquals(expected, cleaned);
    }
    
    @Test
    public void testDifferenceWithEqualStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abcd";
        String s2 = "abcd";
        int expected = 4;
        
        int diff = SoundexUtils.difference(encoder, s1, s2);
        
        assertEquals(expected, diff);
    }
    
    @Test
    public void testDifferenceWithDifferentStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abcd";
        String s2 = "efgh";
        int expected = 0;
        
        int diff = SoundexUtils.difference(encoder, s1, s2);
        
        assertEquals(expected, diff);
    }
    
    @Test
    public void testDifferenceEncodedWithEqualStrings() {
        String es1 = "1234";
        String es2 = "1234";
        int expected = 4;
        
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(expected, diff);
    }
    
    @Test
    public void testDifferenceEncodedWithDifferentStrings() {
        String es1 = "1234";
        String es2 = "5678";
        int expected = 0;
        
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        
        assertEquals(expected, diff);
    }
}