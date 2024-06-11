
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

    private Soundex soundex = new Soundex();

    @Test
    public void testEncode_String() {
        String encoded = soundex.encode("Hello");
        Assert.assertEquals("H400", encoded);
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        String encoded = (String) soundex.encode((Object) "World");
        Assert.assertEquals("W643", encoded);
    }

    @Test
    public void testDifference() throws EncoderException {
        int difference = soundex.difference("Hello", "World");
        Assert.assertEquals(2, difference);
    }

    @Test
    public void testSoundex() {
        String soundexCode = soundex.soundex("Apache");
        Assert.assertEquals("A120", soundexCode);
    }

}
