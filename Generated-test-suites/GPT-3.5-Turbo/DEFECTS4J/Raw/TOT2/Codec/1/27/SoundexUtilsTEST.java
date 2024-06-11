import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "TeSt123&%^$";
        String expectedOutput = "TEST";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
    }

    @Test
    public void testCleanWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, SoundexUtils.clean(input));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Test1";
        String s2 = "Test2";
        int expectedDifference = 3;
        assertEquals(expectedDifference, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceWithNullEncoder() throws EncoderException {
        StringEncoder encoder = null;
        String s1 = "Test1";
        String s2 = "Test2";
        // Expected exception: EncoderException
        assertThrows(EncoderException.class, () -> {
            SoundexUtils.difference(encoder, s1, s2);
        });
    }

    @Test
    public void testDifferenceEncoded() {
        String es1 = "1234";
        String es2 = "12AB";
        int expectedDifference = 2;
        assertEquals(expectedDifference, SoundexUtils.differenceEncoded(es1, es2));
    }

    @Test
    public void testDifferenceEncodedWithNullInput() {
        String es1 = null;
        String es2 = "abcd";
        int expectedDifference = 0;
        assertEquals(expectedDifference, SoundexUtils.differenceEncoded(es1, es2));
    }

}