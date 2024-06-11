
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Smith", "Smyth");
        Assert.assertEquals(2, difference);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object encoded = soundex.encode("Smith");
        Assert.assertEquals("S530", encoded);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Smith");
        Assert.assertEquals("S530", encoded);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(4, maxLength);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(5, maxLength);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex("Smith");
        Assert.assertEquals("S530", encoded);
    }
}
