import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("A261", soundex.encode("Ashcroft"));
        assertEquals("P236", soundex.encode("Petersen"));
        assertEquals("R365", soundex.encode("Roberts"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Ashcroft", "Ashcroft"));
        assertEquals(2, soundex.difference("Ashcroft", "Roberts"));
        assertEquals(4, soundex.difference("Ashcroft", "Petersen"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A261", soundex.soundex("Ashcroft"));
        assertEquals("P236", soundex.soundex("Petersen"));
        assertEquals("R365", soundex.soundex("Roberts"));
    }

}