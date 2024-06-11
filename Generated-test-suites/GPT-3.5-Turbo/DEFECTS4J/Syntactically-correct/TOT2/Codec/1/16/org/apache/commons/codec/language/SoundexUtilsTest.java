import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Hello123World";
        String expected = "HELLOWORLD";
        assertEquals(expected, SoundexUtils.clean(input));
        
        input = "";
        expected = "";
        assertEquals(expected, SoundexUtils.clean(input));
        
        input = null;
        expected = null;
        assertEquals(expected, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Halo";
        int expected = 2;
        try {
            assertEquals(expected, SoundexUtils.difference(encoder, s1, s2));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
        
        s1 = null;
        s2 = "World";
        try {
            SoundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "12345";
        String es2 = "123";
        int expected = 3;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = null;
        es2 = "ABCDE";
        expected = 0;
        assertEquals(expected, SoundexUtils.differenceEncoded(es1, es2));
    }

}