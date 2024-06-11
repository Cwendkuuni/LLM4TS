import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.language.Soundex;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("abc123"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(2, SoundexUtils.difference(soundex, "hello", "hallo"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(3, SoundexUtils.differenceEncoded("1234", "1235"));
    }
    
    @Test
    public void testCleanWithEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testCleanWithNullString() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifferenceWithNullEncoder() {
        try {
            SoundexUtils.difference(null, "hello", "hallo");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testDifferenceEncodedWithNullStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, SoundexUtils.difference(soundex, "", ""));
    }

    @Test
    public void testDifferenceEncodedWithEmptyStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

}