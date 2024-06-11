import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexEncode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("hello");
        assertEquals("H400", encoded);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("hello", "hallo");
        assertEquals(2, difference);
    }

    @Test
    public void testCustomSoundexMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex customSoundex = new Soundex(customMapping);
        String encoded = customSoundex.encode("hello");
        assertEquals("H0010", encoded);
    }

    @Test
    public void testSoundexInvalidInput() {
        Soundex soundex = new Soundex();
        assertThrows(EncoderException.class, () -> {
            soundex.encode(123); // Passing a non-string object
        });
    }

    @Test
    public void testSoundexMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

}