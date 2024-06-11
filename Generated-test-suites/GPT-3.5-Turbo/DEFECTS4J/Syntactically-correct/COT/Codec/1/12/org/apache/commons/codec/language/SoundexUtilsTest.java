import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testClean_basic() {
        String str = "Hello, World!";
        String expectedResult = "HELLOWORLD";
        String actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testClean_emptyString() {
        String str = "";
        String expectedResult = "";
        String actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testClean_nullString() {
        String str = null;
        String expectedResult = null;
        String actualResult = SoundexUtils.clean(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifference_basic() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Hello";
        String s2 = "Hallo";
        int expectedResult = 2;
        int actualResult = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_invalidEncoder() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "Hello";
        String s2 = "Hallo";
        SoundexUtils.difference(encoder, s1, s2);
    }

    @Test
    public void testDifferenceEncoded_basic() {
        String es1 = "H400";
        String es2 = "H420";
        int expectedResult = 2;
        int actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifferenceEncoded_emptyString() {
        String es1 = "";
        String es2 = "";
        int expectedResult = 0;
        int actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDifferenceEncoded_nullString() {
        String es1 = null;
        String es2 = null;
        int expectedResult = 0;
        int actualResult = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expectedResult, actualResult);
    }

}