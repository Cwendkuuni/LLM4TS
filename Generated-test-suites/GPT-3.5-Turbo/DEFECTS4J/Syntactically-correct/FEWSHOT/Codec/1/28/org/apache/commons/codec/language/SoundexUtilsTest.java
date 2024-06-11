import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("ABCD", SoundexUtils.clean("AbC       d!!"));
        assertEquals("123", SoundexUtils.clean("123"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new MyEncoder(), "ABCD", "abcd"));
        assertEquals(0, SoundexUtils.difference(new MyEncoder(), "123", "456"));
        assertEquals(3, SoundexUtils.difference(new MyEncoder(), "abc", "abz"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "abcd"));
        assertEquals(0, SoundexUtils.differenceEncoded("123", "456"));
        assertEquals(3, SoundexUtils.differenceEncoded("abc", "abz"));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "abcd"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", null));
    }

    // Helper class to simulate an encoder
    private static class MyEncoder implements StringEncoder {
        public String encode(String str) {
            if (str == null) {
                return null;
            }
            return str.toUpperCase();
        }
    }
}