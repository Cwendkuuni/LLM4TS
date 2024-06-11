import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String str = "Hello World";
        assertEquals("HELLOWORLD", SoundexUtils.clean(str));

        // Edge case: empty string
        str = "";
        assertEquals("", SoundexUtils.clean(str));

        // Error scenario: null input
        str = null;
        assertEquals(null, SoundexUtils.clean(str));

        // Error scenario: string with no letters
        str = "1234";
        assertEquals("", SoundexUtils.clean(str));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        
        // Typical use case: Strings are the same
        String s1 = "Hello";
        String s2 = "Hello";
        assertEquals(5, SoundexUtils.difference(encoder, s1, s2));

        // Typical use case: Strings have some similarity
        s1 = "Hello";
        s2 = "Halo";
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));

        // Typical use case: Strings have no similarity
        s1 = "Hello";
        s2 = "World";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        // Edge case: Strings are empty
        s1 = "";
        s2 = "";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        // Edge case: Strings have one character
        s1 = "A";
        s2 = "B";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));

        // Error scenario: null String
        s1 = null;
        s2 = "Hello";
        assertEquals(0, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case: Encoded Strings have some similarity
        String es1 = "H400";
        String es2 = "H410";
        assertEquals(2, SoundexUtils.differenceEncoded(es1, es2));

        // Typical use case: Encoded Strings have no similarity
        es1 = "H400";
        es2 = "W800";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: Encoded Strings are empty
        es1 = "";
        es2 = "";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        // Edge case: Encoded Strings have one character
        es1 = "4";
        es2 = "5";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));

        // Error scenario: null Encoded String
        es1 = null;
        es2 = "H400";
        assertEquals(0, SoundexUtils.differenceEncoded(es1, es2));
    }
}