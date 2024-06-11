import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "ABC 123 def.,";
        String expected = "ABCDEF";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Halley";
        int expected = 2;
        try {
            int result = SoundexUtils.difference(encoder, s1, s2);
            assertEquals(expected, result);
        } catch (EncoderException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "1243";
        int expected = 2;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

}