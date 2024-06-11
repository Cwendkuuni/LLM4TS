import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void encode_shouldReturnValidSoundexCode_whenValidStringIsProvided() {
        String result = soundex.encode("Apache");
        
        Assert.assertEquals("A120", result);
    }

    @Test
    public void encode_shouldReturnEmptyString_whenNullStringIsProvided() {
        String result = soundex.encode(null);
        
        Assert.assertEquals("", result);
    }

    @Test
    public void encode_shouldReturnEmptyString_whenEmptyStringIsProvided() {
        String result = soundex.encode("");
        
        Assert.assertEquals("", result);
    }

    @Test
    public void encode_shouldReturnValidSoundexCode_whenStringWithDigitsIsProvided() {
        String result = soundex.encode("1234");
        
        Assert.assertEquals("1", result);
    }

    @Test(expected = EncoderException.class)
    public void encode_shouldThrowEncoderException_whenNonStringObjectIsProvided() {
        soundex.encode(new Object());
    }

    @Test
    public void difference_shouldReturnValidDifferenceValue_whenValidStringsAreProvided() throws EncoderException {
        int result = soundex.difference("Apache", "Appache");
        
        Assert.assertEquals(3, result);
    }

    @Test
    public void difference_shouldReturnZero_whenBothStringsAreNull() throws EncoderException {
        int result = soundex.difference(null, null);
        
        Assert.assertEquals(0, result);
    }

    @Test(expected = EncoderException.class)
    public void difference_shouldThrowEncoderException_whenOneOfStringIsNull() throws EncoderException {
        soundex.difference("Apache", null);
    }

    @Test
    public void difference_shouldReturnZero_whenBothStringsAreEmpty() throws EncoderException {
        int result = soundex.difference("", "");
        
        Assert.assertEquals(0, result);
    }

    @Test
    public void difference_shouldReturnZero_whenBothStringsAreSimilar() throws EncoderException {
        int result = soundex.difference("Apache", "Apache");
        
        Assert.assertEquals(4, result);
    }

}