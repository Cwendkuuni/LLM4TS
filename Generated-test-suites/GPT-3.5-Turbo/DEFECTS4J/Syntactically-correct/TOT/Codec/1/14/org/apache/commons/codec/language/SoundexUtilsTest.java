import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case: input string contains lowercase and uppercase letters
        String str = "HoW aRe yOU";
        Assert.assertEquals("HWAREYOU", SoundexUtils.clean(str));

        // Edge case: input string is empty
        str = "";
        Assert.assertEquals("", SoundexUtils.clean(str));

        // Error scenario: input string is null
        str = null;
        Assert.assertNull(SoundexUtils.clean(str));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case: two similar strings with expected difference value of 4
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hello";
        Assert.assertEquals(4, SoundexUtils.difference(encoder, s1, s2));

        // Edge case: two completely different strings with expected difference value of 0
        s1 = "abc";
        s2 = "def";
        Assert.assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        // Error scenario: encoder throws an EncoderException
        encoder = new RefinedSoundex();
        s1 = "hello";
        s2 = "world";
        try {
            SoundexUtils.difference(encoder, s1, s2);
            Assert.fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case: two similar encoded strings with expected difference value of 4
        String es1 = "H400";
        String es2 = "H400";
        Assert.assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: two completely different encoded strings with expected difference value of 0
        es1 = "A000";
        es2 = "B111";
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        // Error scenario: one encoded string is null
        es1 = "D900";
        es2 = null;
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));
    }
}