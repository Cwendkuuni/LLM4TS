import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_null() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testClean_emptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testClean_noLetters() {
        assertEquals("1234", SoundexUtils.clean("1234"));
    }

    @Test
    public void testClean_mixedCase() {
        assertEquals("HELLO", SoundexUtils.clean("hElLo"));
    }

    @Test
    public void testClean_nonEnglishChars() {
        assertEquals("ABCXYZ", SoundexUtils.clean("abçxyz"));
    }

    @Test
    public void testDifference_sameEncoder() throws EncoderException {
        StringEncoder encoder = new MyEncoder();
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(encoder, "hello", "hello"));
        assertEquals(2, SoundexUtils.difference(encoder, "hello", "hullo"));
        assertEquals(1, SoundexUtils.difference(encoder, "hello", "hella"));
    }

    @Test
    public void testDifference_differentEncoder() throws EncoderException {
        StringEncoder encoder1 = new MyEncoder();
        StringEncoder encoder2 = new MyEncoder();
        assertEquals(0, SoundexUtils.difference(encoder1, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(encoder1, "hello", "hello"));
        assertEquals(2, SoundexUtils.difference(encoder1, "hello", "hullo"));
        assertEquals(1, SoundexUtils.difference(encoder1, "hello", "hella"));        
        assertEquals(0, SoundexUtils.difference(encoder2, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(encoder2, "hello", "hello"));
        assertEquals(2, SoundexUtils.difference(encoder2, "hello", "hullo"));
        assertEquals(1, SoundexUtils.difference(encoder2, "hello", "hella"));
    }

    @Test
    public void testDifferenceEncoded_nullStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, "hello"));
        assertEquals(0, SoundexUtils.differenceEncoded("hello", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncoded_sameLength() {
        assertEquals(0, SoundexUtils.differenceEncoded("abc", "def"));
        assertEquals(1, SoundexUtils.differenceEncoded("abc", "dbc"));
        assertEquals(3, SoundexUtils.differenceEncoded("abc", "abc"));
    }

    @Test
    public void testDifferenceEncoded_differentLength() {
        assertEquals(0, SoundexUtils.differenceEncoded("abc", "defg"));
        assertEquals(1, SoundexUtils.differenceEncoded("abc", "dbch"));
        assertEquals(3, SoundexUtils.differenceEncoded("xyz", "xyzabc"));
    }

    private static class MyEncoder implements StringEncoder {
        public String encode(String s) throws EncoderException {
            return s;
        }
    }

}