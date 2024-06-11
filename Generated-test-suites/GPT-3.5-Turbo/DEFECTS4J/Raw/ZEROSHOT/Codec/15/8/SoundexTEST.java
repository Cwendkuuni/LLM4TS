import org.junit.Test;
import org.junit.Assert;

public class SoundexTest {
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("T000", soundex.soundex("test"));
        Assert.assertEquals("T000", soundex.soundex("Test"));
        Assert.assertEquals("C640", soundex.soundex("coding"));
        Assert.assertEquals("C640", soundex.soundex("Coding"));
    }
    
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("T000", soundex.encode("test"));
        Assert.assertEquals("T000", soundex.encode("Test"));
        Assert.assertEquals("C640", soundex.encode("coding"));
        Assert.assertEquals("C640", soundex.encode("Coding"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(4, soundex.difference("test", "test"));
        Assert.assertEquals(4, soundex.difference("Test", "Test"));
        Assert.assertEquals(0, soundex.difference("coding", "test"));
        Assert.assertEquals(0, soundex.difference("Coding", "Test"));
    }
}