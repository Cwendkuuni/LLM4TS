import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_typicalCase() {
        String input = "Ab23#CD";
        String expectedOutput = "ABCD";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_edgeCase() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_errorCase() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference_typicalCase() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "abcd";
        String s2 = "abdc";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference_edgeCase() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "ABCD";
        String s2 = "ABCD";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_errorCase() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = null;
        String s2 = "abcd";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded_typicalCase() {
        String es1 = "abcd";
        String es2 = "abdc";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded_edgeCase() {
        String es1 = "ABCD";
        String es2 = "ABCD";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded_errorCase() {
        String es1 = null;
        String es2 = "abcd";
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

}