import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Apache");
        assertEquals("A120", encoded);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Apache", "Appache");
        assertEquals(3, difference);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testEncodeWithEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("");
        assertEquals("", encoded);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertArrayEquals("01230120022455012623010202".toCharArray(), mapping);
    }
}