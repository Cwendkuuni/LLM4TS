import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("FOOBAR", SoundexUtils.clean("foo bar"));
        assertEquals("BAZ", SoundexUtils.clean(" b a z "));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() {
        // Test case with both strings null
        assertEquals(0, SoundexUtils.difference(new StringEncoderImpl(), null, null));

        // Test case with one string null
        assertEquals(0, SoundexUtils.difference(new StringEncoderImpl(), "abc", null));

        // Test case with empty strings
        assertEquals(0, SoundexUtils.difference(new StringEncoderImpl(), "", ""));

        // Test case with same strings
        assertEquals(4, SoundexUtils.difference(new StringEncoderImpl(), "abc", "abc"));

        // Test case with different strings
        assertEquals(2, SoundexUtils.difference(new StringEncoderImpl(), "abc", "abd"));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test case with both strings null
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // Test case with one string null
        assertEquals(0, SoundexUtils.differenceEncoded("ABC", null));

        // Test case with empty strings
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));

        // Test case with same strings
        assertEquals(4, SoundexUtils.differenceEncoded("ABC", "ABC"));

        // Test case with different strings
        assertEquals(2, SoundexUtils.differenceEncoded("ABC", "ABD"));
    }
}