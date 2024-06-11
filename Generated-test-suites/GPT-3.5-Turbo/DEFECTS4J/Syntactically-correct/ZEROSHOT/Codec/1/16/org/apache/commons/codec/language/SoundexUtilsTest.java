import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        Assert.assertEquals("HELLO", SoundexUtils.clean("Hello!"));
        Assert.assertEquals("WORLD", SoundexUtils.clean("world"));
        Assert.assertEquals("ABC", SoundexUtils.clean("ABC123"));
        Assert.assertEquals("XYZ", SoundexUtils.clean("xYz"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        Assert.assertEquals(4, SoundexUtils.difference(encoder, "hello", "hello"));
        Assert.assertEquals(3, SoundexUtils.difference(encoder, "hello", "hell"));
        Assert.assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
    }

    @Test
    public void testDifferenceEncoded() {
        Assert.assertEquals(4, SoundexUtils.differenceEncoded("HELLO", "HELLO"));
        Assert.assertEquals(3, SoundexUtils.differenceEncoded("HELLO", "HELL"));
        Assert.assertEquals(0, SoundexUtils.differenceEncoded("HELLO", "WORLD"));
    }
}