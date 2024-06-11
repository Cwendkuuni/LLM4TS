
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode_ObjectIsNotString_ThrowsEncoderException() {
        Soundex soundex = new Soundex();
        
        try {
            soundex.encode(1234);
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            //Successfully caught the expected exception
        }
    }
    
    @Test
    public void testEncode_EncodesStringUsingSoundexAlgorithm() {
        Soundex soundex = new Soundex();
        
        String encoded = soundex.encode("Apache");
        
        Assert.assertEquals("A120", encoded);
    }
    
    @Test
    public void testEncode_EmptyString_ReturnsEmptyString() {
        Soundex soundex = new Soundex();
        
        String encoded = soundex.encode("");
        
        Assert.assertEquals("", encoded);
    }
    
    @Test
    public void testEncode_StringWithNonMappedCharacters_ThrowsIllegalArgumentException() {
        Soundex soundex = new Soundex();
        
        try {
            soundex.encode("Hello World!");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Successfully caught the expected exception
        }
    }
    
    @Test
    public void testDifference_EncodesAndComparesStrings_ReturnsNumberOfMatchingCharacters() throws EncoderException {
        Soundex soundex = new Soundex();
        
        int difference = soundex.difference("Apache", "Appache");
        
        Assert.assertEquals(3, difference);
    }
    
    @Test
    public void testDifference_EncodesAndComparesStrings_ReturnsZeroForDifferentStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        
        int difference = soundex.difference("Apache", "Microsoft");
        
        Assert.assertEquals(0, difference);
    }
    
    @Test
    public void testDifference_NullStrings_ThrowsNullPointerException() throws EncoderException {
        Soundex soundex = new Soundex();
        
        try {
            soundex.difference(null, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //Successfully caught the expected exception
        }
    }
    
    @Test
    public void testDifference_StringsWithDifferentLengths_ThrowsIllegalArgumentException() throws EncoderException {
        Soundex soundex = new Soundex();
        
        try {
            soundex.difference("Java", "JavaScript");
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Successfully caught the expected exception
        }
    }
    
}
