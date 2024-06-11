import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Apache");
        assertEquals("A120", result);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Apache", "Appache");
        assertEquals(3, result);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Apache");
        assertEquals("A120", result);
    }

}