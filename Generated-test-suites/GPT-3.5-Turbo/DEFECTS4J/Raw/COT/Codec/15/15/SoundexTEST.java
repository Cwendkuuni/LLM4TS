
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode_String() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("S530", soundex.encode("Smith"));
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals("S530", soundex.encode((Object) "Smith"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(4, soundex.difference("Smith", "Smyth"));
    }

}
