import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {
    
    private Soundex soundex;

    @Test
    public void testEncode() {
        soundex = new Soundex();
        String result = soundex.encode("Hello");
        Assert.assertEquals("H400", result);
    }

    @Test
    public void testNullEncode() {
        soundex = new Soundex();
        String result = soundex.encode(null);
        Assert.assertNull(result);
    }

    @Test
    public void testEmptyEncode() {
        soundex = new Soundex();
        String result = soundex.encode("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testDifference() throws EncoderException {
        soundex = new Soundex();
        int result = soundex.difference("Hello", "hEllo");
        Assert.assertEquals(3, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonString() throws EncoderException {
        soundex = new Soundex();
        soundex.difference(123, "Hello");
    }

    @Test
    public void testSoundex() {
        soundex = new Soundex();
        String result = soundex.soundex("Hello");
        Assert.assertEquals("H400", result);
    }

    @Test
    public void testSoundexWithNullString() {
        soundex = new Soundex();
        String result = soundex.soundex(null);
        Assert.assertNull(result);
    }

    @Test
    public void testSoundexWithEmptyString() {
        soundex = new Soundex();
        String result = soundex.soundex("");
        Assert.assertEquals("", result);
    }

}