import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("TEST", SoundexUtils.clean("Test123"));
    }

    @Test
    public void testCleanWithNull() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testCleanWithEmptyString() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, SoundexUtils.difference(soundex, "Apache", "APACHE"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1234"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithEncoderException() throws EncoderException {
        Soundex soundex = new Soundex();
        SoundexUtils.difference(soundex, null, "Test");
    }
}