import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "Abc123DEF!@#";
        String expectedOutput = "ABCDE";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
    }

    @Test
    public void testCleanEmptyString() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = new Soundex();
        String s1 = "Apache";
        String s2 = "Appache";
        int expectedDifference = 2;
        try {
            assertEquals(expectedDifference, SoundexUtils.difference(encoder, s1, s2));
        } catch (EncoderException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "A12X";
        int expectedDifference = 3;
        assertEquals(expectedDifference, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncodedNullStrings() {
        String es1 = null;
        String es2 = "A12X";
        int expectedDifference = 0;
        assertEquals(expectedDifference, SoundexUtils.differenceEncoded(es1, es2));
    }

}