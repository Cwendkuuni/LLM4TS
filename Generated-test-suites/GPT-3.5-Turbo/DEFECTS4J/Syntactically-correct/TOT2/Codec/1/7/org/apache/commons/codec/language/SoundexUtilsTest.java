import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    private SoundexUtils soundexUtils;

    @Before
    public void setUp() {
        soundexUtils = new SoundexUtils();
    }

    @Test
    public void testClean() {
        String input = "abc123XYZ!@#";
        String expected = "ABCXYZ";
        assertEquals(expected, soundexUtils.clean(input));
    }

    @Test
    public void testCleanWithEmptyInput() {
        String input = "";
        String expected = "";
        assertEquals(expected, soundexUtils.clean(input));
    }

    @Test
    public void testCleanWithNullInput() {
        String input = null;
        assertNull(soundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = "hallo";
        int expected = 3;
        assertEquals(expected, soundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceWithNullEncoder() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "hello";
        String s2 = "hallo";
        try {
            soundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDifferenceWithNullString() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "hello";
        String s2 = null;
        try {
            soundexUtils.difference(encoder, s1, s2);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "1243";
        int expected = 2;
        assertEquals(expected, soundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncodedWithNullString() {
        String es1 = "1234";
        String es2 = null;
        int expected = 0;
        assertEquals(expected, soundexUtils.differenceEncoded(es1, es2));
    }

}