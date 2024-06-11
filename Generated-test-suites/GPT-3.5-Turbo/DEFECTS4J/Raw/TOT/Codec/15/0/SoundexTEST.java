import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    private static Soundex soundex;

    @BeforeClass
    public static void setUp() {
        soundex = new Soundex();
    }

    // Testing difference method

    // Typical Use Case
    @Test
    public void testDifference_typical() throws Exception {
        int result = soundex.difference("happy", "hoppy");
        assertEquals(2, result);
    }

    // Edge Case: Strings with same letters
    @Test
    public void testDifference_sameLetters() throws Exception {
        int result = soundex.difference("aa", "aa");
        assertEquals(4, result);
    }

    // Edge Case: Strings with no common letters
    @Test
    public void testDifference_noCommonLetters() throws Exception {
        int result = soundex.difference("abc", "def");
        assertEquals(0, result);
    }

    // Testing encode methods

    // Typical Use Case
    @Test
    public void testEncode_typical() throws Exception {
        String result = soundex.encode("happy");
        assertEquals("H100", result);
    }

    // Edge Case: Empty String
    @Test
    public void testEncode_emptyString() throws Exception {
        String result = soundex.encode("");
        assertEquals("", result);
    }

    // Error Scenario: Object is not a String
    @Test(expected = EncoderException.class)
    public void testEncode_nonStringObject() throws Exception {
        soundex.encode(new Integer(5));
    }

    // Testing soundex method

    // Typical Use Case
    @Test
    public void testSoundex_typical() throws Exception {
        String result = soundex.soundex("monkey");
        assertEquals("M520", result);
    }

    // Edge Case: String with single letter
    @Test
    public void testSoundex_singleLetter() throws Exception {
        String result = soundex.soundex("a");
        assertEquals("A000", result);
    }

    // Edge Case: String with non-alphabetic characters
    @Test
    public void testSoundex_nonAlphabetic() throws Exception {
        String result = soundex.soundex("123");
        assertEquals("0000", result);
    }

    // Error Scenario: String is null
    @Test
    public void testSoundex_nullString() throws Exception {
        String result = soundex.soundex(null);
        assertNull(result);
    }

}