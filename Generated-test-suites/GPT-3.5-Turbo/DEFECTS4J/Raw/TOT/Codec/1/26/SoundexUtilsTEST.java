import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String str = "Hello World";
        String cleanedStr = SoundexUtils.clean(str);
        assertEquals("HELLOWORLD", cleanedStr);

        // Edge case: empty string
        str = "";
        cleanedStr = SoundexUtils.clean(str);
        assertEquals("", cleanedStr);

        // Error scenario: null string
        str = null;
        cleanedStr = SoundexUtils.clean(str);
        assertNull(cleanedStr);
    }

    @Test
    public void testDifference() throws EncoderException {
        // Typical use case
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "world";
        int difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(1, difference);

        // Edge case: same input string
        s1 = "hello";
        s2 = "hello";
        difference = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, difference);

        // Error scenario: null input string
        s1 = null;
        s2 = "world";
        try {
            SoundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "H400";
        String es2 = "W376";
        int difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(1, difference);

        // Edge case: same encoded string
        es1 = "H400";
        es2 = "H400";
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(4, difference);

        // Error scenario: null encoded string
        es1 = null;
        es2 = "W376";
        difference = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(0, difference);
    }

}