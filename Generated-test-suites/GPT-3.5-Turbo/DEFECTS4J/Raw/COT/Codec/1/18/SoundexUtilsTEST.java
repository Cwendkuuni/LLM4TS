import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test null input
        String str = null;
        String expected = null;
        String actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
        
        // Test empty input
        str = "";
        expected = "";
        actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
        
        // Test input with non-letter characters
        str = "123abc!@#";
        expected = "ABC";
        actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in lower case
        str = "abcdefghijklmnopqrstuvwxyz";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in upper case
        str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
        
        // Test input with mixture of upper case and lower case letter characters
        str = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        actual = SoundexUtils.clean(str);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        // Test null input
        StringEncoder encoder = null;
        String s1 = null;
        String s2 = null;
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
        
        // Test empty input
        encoder = new Soundex();
        s1 = "";
        s2 = "";
        expected = 0;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
        
        // Test input with non-letter characters
        encoder = new Soundex();
        s1 = "123abc!@#";
        s2 = "456def$%^";
        expected = 0;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in lower case
        encoder = new Soundex();
        s1 = "abcdefghijklmnopqrstuvwxyz";
        s2 = "abcdefghijklmnopqrstuvwxyz";
        expected = 4;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in upper case
        encoder = new Soundex();
        s1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        s2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        expected = 4;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
        
        // Test input with mixture of upper case and lower case letter characters
        encoder = new Soundex();
        s1 = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        s2 = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        expected = 4;
        actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDifferenceEncoded() {
        // Test null input
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
        
        // Test empty input
        es1 = "";
        es2 = "";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
        
        // Test input with non-letter characters
        es1 = "123abc!@#";
        es2 = "456def$%^";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in lower case
        es1 = "abcdefghijklmnopqrstuvwxyz";
        es2 = "abcdefghijklmnopqrstuvwxyz";
        expected = 4;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
        
        // Test input with all letter characters in upper case
        es1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        es2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        expected = 4;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
        
        // Test input with mixture of upper case and lower case letter characters
        es1 = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        es2 = "aBcDeFgHiJkLmNoPqRsTuVwXyZ";
        expected = 4;
        actual = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}