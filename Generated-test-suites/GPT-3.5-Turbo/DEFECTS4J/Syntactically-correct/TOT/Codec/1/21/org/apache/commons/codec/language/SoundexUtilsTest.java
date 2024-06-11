import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_typicalInput() {
        String input = "Hello World";
        String expectedOutput = "HELLOWORLD";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_emptyInput() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testClean_nullInput() {
        String input = null;
        String expectedOutput = null;
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifference_typicalInput() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hallo";
        int expectedOutput = 4;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_encoderException() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "hello";
        String s2 = "hallo";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded_typicalInput() {
        String es1 = "H400";
        String es2 = "H401";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceEncoded_nullInput() {
        String es1 = null;
        String es2 = null;
        int expectedOutput = 0;
        int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedOutput, actualOutput);
    }
}