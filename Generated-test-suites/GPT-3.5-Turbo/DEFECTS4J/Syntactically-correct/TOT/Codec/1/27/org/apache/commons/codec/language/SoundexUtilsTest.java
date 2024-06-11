import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        Assert.assertEquals("CLEAN", SoundexUtils.clean("clean"));

        // Edge case: str is null
        Assert.assertNull(SoundexUtils.clean(null));

        // Edge case: str is empty
        Assert.assertEquals("", SoundexUtils.clean(""));

        // Error scenario: str contains non-letter characters
        Assert.assertEquals("ABC", SoundexUtils.clean("ab@!c123"));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case
        Assert.assertEquals(4, SoundexUtils.difference(new MyEncoder(), "abcd", "abcd"));

        // Edge case: s1 and s2 are null
        Assert.assertEquals(0, SoundexUtils.difference(new MyEncoder(), null, null));

        // Edge case: s1 and s2 are empty
        Assert.assertEquals(0, SoundexUtils.difference(new MyEncoder(), "", ""));

        // Edge case: s1 and s2 are different lengths
        Assert.assertEquals(3, SoundexUtils.difference(new MyEncoder(), "abc", "abcd"));

        // Error scenario: EncoderException
        Assert.assertThrows(EncoderException.class, () -> SoundexUtils.difference(new MyEncoder(true), "abcd", "abcd"));
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        Assert.assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCE"));

        // Edge case: es1 and es2 are null
        Assert.assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // Edge case: es1 and es2 are empty
        Assert.assertEquals(0, SoundexUtils.differenceEncoded("", ""));

        // Edge case: es1 and es2 are different lengths
        Assert.assertEquals(3, SoundexUtils.differenceEncoded("ABC", "ABCD"));
    }

    private class MyEncoder implements StringEncoder {

        private boolean throwException = false;

        public MyEncoder() {
        }

        public MyEncoder(boolean throwException) {
            this.throwException = throwException;
        }

        @Override
        public String encode(String s) throws EncoderException {
            if (throwException) {
                throw new EncoderException("Encoder Exception");
            }
            return s.toUpperCase();
        }
    }
}