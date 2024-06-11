import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "abc123";
        String expectedOutput = "ABC";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
        
        input = "aa bb cc";
        expectedOutput = "AA BB CC";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
        
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hallo";
        int expectedOutput = 3;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "abc";
        s2 = "def";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
        
        s1 = "Apache";
        s2 = "aP$che";
        expectedOutput = 6;
        assertEquals(expectedOutput, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "H400";
        String es2 = "H410";
        int expectedOutput = 3;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = "";
        es2 = "abc";
        expectedOutput = 0;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
        
        es1 = "XYZ";
        es2 = "XYZ";
        expectedOutput = 3;
        assertEquals(expectedOutput, SoundexUtils.differenceEncoded(es1, es2));
    }
}

