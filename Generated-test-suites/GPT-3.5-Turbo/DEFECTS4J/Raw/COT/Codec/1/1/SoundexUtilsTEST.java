
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "aBc123";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abc";
        String s2 = "def";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}
