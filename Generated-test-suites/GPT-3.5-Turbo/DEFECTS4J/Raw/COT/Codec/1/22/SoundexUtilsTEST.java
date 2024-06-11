
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        // Test null input
        assertNull(SoundexUtils.clean(null));
        
        // Test empty string input
        assertEquals("", SoundexUtils.clean(""));
        
        // Test upper case letters only
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        
        // Test upper and lower case letters
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        
        // Test input with non-letter characters
        assertEquals("ABC", SoundexUtils.clean("A. BC?"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        
        // Test same encoded strings
        assertEquals(4, SoundexUtils.difference(encoder, "ABCD", "ABCD"));
        
        // Test different encoded strings
        assertEquals(2, SoundexUtils.difference(encoder, "ABCD", "ABEF"));
        
        // Test one null encoded string
        assertEquals(0, SoundexUtils.difference(encoder, null, "ABCD"));
        
        // Test both null encoded strings
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }
    
    @Test
    public void testDifferenceEncoded() {
        // Test same encoded strings
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        
        // Test different encoded strings
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABEF"));
        
        // Test one null encoded string
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABCD"));
        
        // Test both null encoded strings
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}
