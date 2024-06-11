import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Basic test case
        Assert.assertEquals(0, soundex.difference("John", "Jane"));
        Assert.assertEquals(2, soundex.difference("John", "Johan"));
        Assert.assertEquals(4, soundex.difference("John", "John"));
        
        // Edge cases
        Assert.assertEquals(0, soundex.difference("", ""));
        Assert.assertEquals(0, soundex.difference(null, null));
        Assert.assertEquals(0, soundex.difference("John", ""));
        Assert.assertEquals(0, soundex.difference("", "John"));
        Assert.assertEquals(0, soundex.difference("John", null));
        Assert.assertEquals(0, soundex.difference(null, "John"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Basic test case
        Assert.assertEquals("J500", soundex.encode("John"));
        Assert.assertEquals("A236", soundex.encode("Alice"));
    
        // Edge cases
        Assert.assertEquals("0000", soundex.encode(""));
        Assert.assertEquals(null, soundex.encode(null));
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        
        // Basic test case
        Assert.assertEquals("J500", soundex.encode("John"));
        Assert.assertEquals("A236", soundex.encode("Alice"));
    
        // Edge cases
        Assert.assertEquals("0000", soundex.encode(""));
        Assert.assertEquals("", soundex.encode(null));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        // Basic test case
        Assert.assertEquals("J500", soundex.soundex("John"));
        Assert.assertEquals("A236", soundex.soundex("Alice"));
    
        // Edge cases
        Assert.assertEquals("0000", soundex.soundex(""));
        Assert.assertEquals(null, soundex.soundex(null));
    }

}
