import org.junit.Test;
import org.apache.commons.codec.DecoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Test String";
        String expectedOutput = "TESTSTRING";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Test1";
        String s2 = "Test2";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "1243";
        int expectedOutput = 2;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceException() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Invalid";
        String s2 = "Test";
        SoundexUtils.difference(encoder, s1, s2);
    }

}