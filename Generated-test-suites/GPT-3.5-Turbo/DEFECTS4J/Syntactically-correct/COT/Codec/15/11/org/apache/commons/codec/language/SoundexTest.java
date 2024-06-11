import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("ABCD", "ABCD");
        Assert.assertEquals(4, result);
    }

    @Test
    public void testEncodeWithNonStringParameter() {
        Soundex soundex = new Soundex();
        Object obj = new Object();
        try {
            soundex.encode(obj);
            Assert.fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("ABCD");
        Assert.assertEquals("A123", result);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int result = soundex.getMaxLength();
        Assert.assertEquals(4, result);
    }

    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] result = soundex.getSoundexMapping();
        Assert.assertNotNull(result);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        int result = soundex.getMaxLength();
        Assert.assertEquals(5, result);
    }

    @Test
    public void testSoundexWithNullInput() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex(null);
        Assert.assertNull(result);
    }

    @Test
    public void testSoundexWithNonAlphabeticCharacters() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("123!@#$");
        Assert.assertEquals("0000", result);
    }

    @Test
    public void testSoundexWithShortInput() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("ABCD");
        Assert.assertEquals("A123", result);
    }

    @Test
    public void testSoundexWithLongInput() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Assert.assertEquals("A123", result);
    }
}