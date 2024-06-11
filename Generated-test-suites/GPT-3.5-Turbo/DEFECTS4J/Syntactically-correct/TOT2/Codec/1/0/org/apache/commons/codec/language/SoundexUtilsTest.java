import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABCD", SoundexUtils.clean("a1$b2CD"));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(null, SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() {
        assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "hello"));
        assertEquals(0, SoundexUtils.difference(new Soundex(), "test", "world"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
        assertEquals(0, SoundexUtils.differenceEncoded("5678", "abcd"));
    }

}