
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        // Typical use case - All uppercase letters
        Assert.assertEquals("ABCD", SoundexUtils.clean("abcd"));
        
        // Edge case - Input string is empty
        Assert.assertEquals("", SoundexUtils.clean(""));
        
        // Edge case - Input string is null
        Assert.assertNull(SoundexUtils.clean(null));
        
        // Error scenario - Input string contains non-letter characters
        Assert.assertEquals("ABCDEFGHIJKLMN", SoundexUtils.clean("ABC123!@#$%^&*()_+-=DEFGHIJKLMN"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        // Typical use case - Encoded strings are different, difference is 2
        StringEncoder encoder = new Soundex();
        Assert.assertEquals(2, SoundexUtils.difference(encoder, "abcd", "efgh"));
        
        // Typical use case - Encoded strings are identical, difference is 4
        StringEncoder refinedEncoder = new RefinedSoundex();
        Assert.assertEquals(4, SoundexUtils.difference(refinedEncoder, "abcd", "abcd"));
        
        // Edge case - Encoded strings have different lengths, difference is minimum of their lengths
        Assert.assertEquals(3, SoundexUtils.difference(encoder, "abcde", "abcd"));
        
        // Error scenario - Encoder throws EncoderException for one of the strings
        StringEncoder faultyEncoder = new FaultyEncoder();
        try {
            SoundexUtils.difference(faultyEncoder, "abcd", "efgh");
            Assert.fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testDifferenceEncoded() {
        // Typical use case - Encoded strings are different, difference is 2
        Assert.assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "EFGH"));
        
        // Typical use case - Encoded strings are identical, difference is 4
        Assert.assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        
        // Edge case - Encoded strings have different lengths, difference is minimum of their lengths
        Assert.assertEquals(3, SoundexUtils.differenceEncoded("ABCDE", "ABCD"));
        
        // Error scenario - One of the encoded strings is null
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(null, "ABCD"));
        
        // Error scenario - One of the encoded strings is empty
        Assert.assertEquals(0, SoundexUtils.differenceEncoded("ABCD", ""));
    }
    
    private static class FaultyEncoder implements StringEncoder {
        @Override
        public Object encode(String source) throws EncoderException {
            throw new EncoderException("Faulty Encoder Exception");
        }
    }
}

