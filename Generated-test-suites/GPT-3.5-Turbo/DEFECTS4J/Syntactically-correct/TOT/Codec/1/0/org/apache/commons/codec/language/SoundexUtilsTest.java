import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void clean_shouldReturnCleanString() {
        String input = "A b C 4";
        String expectedOutput = "ABC";
        
        String actualOutput = SoundexUtils.clean(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void clean_shouldReturnUpperCaseString() {
        String input = "abc";
        String expectedOutput = "ABC";
        
        String actualOutput = SoundexUtils.clean(input);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void clean_shouldReturnNullForNullInput() {
        String input = null;
        
        String actualOutput = SoundexUtils.clean(input);
        
        Assert.assertNull(actualOutput);
    }
    
    @Test
    public void difference_shouldReturnZeroForDifferentEncodedStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();

        String s1 = "abc";
        String s2 = "def";
        int expectedOutput = 0;
        
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void difference_shouldReturnFourForIdenticalSoundexEncodedStrings() throws EncoderException {
        StringEncoder encoder = new Soundex();

        String s1 = "abc";
        String s2 = "abc";
        int expectedOutput = 4;
        
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void difference_shouldReturnGreaterThanFourForIdenticalRefinedSoundexEncodedStrings() throws EncoderException {
        StringEncoder encoder = new RefinedSoundex();

        String s1 = "abc";
        String s2 = "abc";
        int expectedOutput = 5;
        
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        
        Assert.assertTrue(actualOutput > 4);
    }
    
    @Test
    public void differenceEncoded_shouldReturnZeroForDifferentEncodedStrings() {
        String es1 = "123";
        String es2 = "456";
        int expectedOutput = 0;
        
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void differenceEncoded_shouldReturnFourForIdenticalEncodedStrings() {
        String es1 = "1234";
        String es2 = "1234";
        int expectedOutput = 4;
        
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}