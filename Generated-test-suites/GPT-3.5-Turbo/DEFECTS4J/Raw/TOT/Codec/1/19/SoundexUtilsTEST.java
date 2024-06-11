import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test typical use case with mixed case letters
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        
        // Test empty string input
        assertEquals("", SoundexUtils.clean(""));
        
        // Test input with no letters
        assertEquals("", SoundexUtils.clean("123"));
        
        // Test input with special characters
        assertEquals("AB", SoundexUtils.clean("a!b"));
    }
    
    @Test
    public void testDifference() {
        // Test typical use case with two similar strings
        try {
            assertEquals(4, SoundexUtils.difference(new Soundex(), "abcd", "ABCD"));
        } catch (EncoderException e) {
            fail("EncoderException was thrown.");
        }
        
        // Test typical use case with two different strings
        try {
            assertEquals(0, SoundexUtils.difference(new Soundex(), "abcd", "efgh"));
        } catch (EncoderException e) {
            fail("EncoderException was thrown.");
        }
        
        // Test input with null strings
        try {
            assertEquals(0, SoundexUtils.difference(new Soundex(), null, "abcd"));
            assertEquals(0, SoundexUtils.difference(new Soundex(), "abcd", null));
        } catch (EncoderException e) {
            fail("EncoderException was thrown.");
        }
    }
    
    @Test
    public void testDifferenceEncoded() {
        // Test typical use case with two similar encoded strings
        assertEquals(4, SoundexUtils.differenceEncoded("abcd", "ABCD"));
        
        // Test typical use case with two different encoded strings
        assertEquals(0, SoundexUtils.differenceEncoded("abcd", "efgh"));
        
        // Test input with null strings
        assertEquals(0, SoundexUtils.differenceEncoded(null, "abcd"));
        assertEquals(0, SoundexUtils.differenceEncoded("abcd", null));
    }
}