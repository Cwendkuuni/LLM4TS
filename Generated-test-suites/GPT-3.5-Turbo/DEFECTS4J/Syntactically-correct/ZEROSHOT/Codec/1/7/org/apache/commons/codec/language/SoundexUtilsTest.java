import org.junit.Test;
import org.junit.Assert;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test null input
        Assert.assertNull(SoundexUtils.clean(null));
        
        // Test empty input
        Assert.assertEquals("", SoundexUtils.clean(""));
        
        // Test all lowercase input
        Assert.assertEquals("HELLO", SoundexUtils.clean("hello"));
        
        // Test all uppercase input
        Assert.assertEquals("HELLO", SoundexUtils.clean("HELLO"));
        
        // Test mixed case input
        Assert.assertEquals("HAPPY", SoundexUtils.clean("hApPy"));
                
        // Test input with non-alphabetic characters
        Assert.assertEquals("HELLO", SoundexUtils.clean("Hi, there! HELLO"));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Test null input
        Assert.assertEquals(0, SoundexUtils.difference(null, null, null));
        
        // Test empty input
        Assert.assertEquals(0, SoundexUtils.difference(null, "", ""));
        
        // Test same encoded Strings
        Assert.assertEquals(4, SoundexUtils.difference(new DummyEncoder(), "AAAA", "AAAA"));
        
        // Test different encoded Strings
        Assert.assertEquals(0, SoundexUtils.difference(new DummyEncoder(), "AAAA", "BBBB"));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test null input
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        
        // Test empty input
        Assert.assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        
        // Test same encoded Strings
        Assert.assertEquals(4, SoundexUtils.differenceEncoded("AAAA", "AAAA"));
        
        // Test different encoded Strings
        Assert.assertEquals(0, SoundexUtils.differenceEncoded("AAAA", "BBBB"));
    }
    
    // Test dummy encoder class for difference method
    private class DummyEncoder implements StringEncoder {
        public String encode(String str) throws EncoderException {
            return str;
        }
    }

}