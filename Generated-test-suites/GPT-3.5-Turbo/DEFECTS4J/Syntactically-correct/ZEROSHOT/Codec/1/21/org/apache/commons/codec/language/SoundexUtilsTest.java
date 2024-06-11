import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test when str is null
        assertEquals(null, SoundexUtils.clean(null));

        // Test when str is empty
        assertEquals("", SoundexUtils.clean(""));

        // Test when str has upper case letters
        assertEquals("ABC", SoundexUtils.clean("aBc"));

        // Test when str has lower case letters
        assertEquals("ABC", SoundexUtils.clean("abc"));

        // Test when str has numbers
        assertEquals("ABC", SoundexUtils.clean("a1b2c"));

        // Test when str has special characters
        assertEquals("ABC", SoundexUtils.clean("a@b#c"));
    }

    @Test
    public void testDifference() throws EncoderException {
        // Create a dummy encoder
        StringEncoder encoder = new StringEncoder() {
            public String encode(String str) throws EncoderException {
                return str.toUpperCase();
            }
        };

        // Test when s1 and s2 are null
        assertEquals(0, SoundexUtils.difference(encoder, null, null));

        // Test when s1 is null and s2 is not null
        assertEquals(0, SoundexUtils.difference(encoder, null, "abc"));

        // Test when s1 is not null and s2 is null
        assertEquals(0, SoundexUtils.difference(encoder, "abc", null));

        // Test when s1 and s2 are not null
        assertEquals(3, SoundexUtils.difference(encoder, "abc", "def"));

        // Test when s1 and s2 are not null and have different lengths
        assertEquals(0, SoundexUtils.difference(encoder, "abc", "de"));

        // Test when s1 and s2 are not null and have same characters
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "ABCD"));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test when es1 and es2 are null
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // Test when es1 is null and es2 is not null
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABCD"));

        // Test when es1 is not null and es2 is null
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", null));

        // Test when es1 and es2 are not null
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "DEFG"));

        // Test when es1 and es2 are not null and have different lengths
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "DEF"));

        // Test when es1 and es2 are not null and have same characters
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
    }
}