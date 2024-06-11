import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();

        // Test cases for soundex method
        Assert.assertEquals("S530", soundex.soundex("Smith"));
        Assert.assertEquals("T522", soundex.soundex("Taylor"));
        Assert.assertEquals("H400", soundex.soundex("Harris"));
        
        // Additional test cases
        Assert.assertEquals("B623", soundex.soundex("Brown"));
        Assert.assertEquals("D235", soundex.soundex("Davis"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();

        // Test cases for encode method
        Assert.assertEquals("S530", soundex.encode("Smith"));
        Assert.assertEquals("T522", soundex.encode("Taylor"));
        Assert.assertEquals("H400", soundex.encode("Harris"));

        // Additional test cases
        Assert.assertEquals("B623", soundex.encode("Brown"));
        Assert.assertEquals("D235", soundex.encode("Davis"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();

        // Test case for encode method with invalid input
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();

        // Test cases for difference method
        Assert.assertEquals(4, soundex.difference("Smith", "Smith"));
        Assert.assertEquals(4, soundex.difference("Taylor", "Taylor"));
        Assert.assertEquals(2, soundex.difference("Harris", "Harrison"));

        // Additional test case
        Assert.assertEquals(3, soundex.difference("Brown", "Brawn"));
    }
}