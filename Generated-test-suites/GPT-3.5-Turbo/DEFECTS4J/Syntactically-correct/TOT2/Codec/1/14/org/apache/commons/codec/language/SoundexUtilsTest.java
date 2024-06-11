import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "Hello123";
        String expected = "HELLO";
        assertEquals(expected, SoundexUtils.clean(input));
        
        // Edge case: empty string
        input = "";
        expected = "";
        assertEquals(expected, SoundexUtils.clean(input));
        
        // Error scenario: null input
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() {
        // Typical use case
        StringEncoder encoder = new RefinedSoundex();
        String s1 = "hello";
        String s2 = "hallo";
        int expected = 3;
        try {
            assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown for valid input");
        }
        
        // Edge case: different input lengths
        s1 = "abc";
        s2 = "xyz";
        expected = 0;
        try {
            assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown for valid input");
        }
        
        // Error scenario: null encoder
        try {
            SoundexUtils.difference(null, s1, s2);
            fail("EncoderException should be thrown for null encoder");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "12345";
        String es2 = "12346";
        int expected = 4;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
        
        // Edge case: shorter encoded string
        es1 = "123";
        es2 = "1234";
        expected = 3;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
        
        // Error scenario: null input
        assertEquals(0, SoundexUtils.differenceEncoded(null, es2));
    }
}