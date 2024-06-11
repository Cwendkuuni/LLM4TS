import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("J250", soundex.encode("Johnson"));
        assertEquals("T522", soundex.encode("Turing"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidType() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Smith", "Johnson"));
        assertEquals(2, soundex.difference("Turing", "Turing"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("J250", soundex.soundex("Johnson"));
        assertEquals("T522", soundex.soundex("Turing"));
    }

    @Test
    public void testGetStringEncoder() throws EncoderException {
        Soundex soundex = new Soundex();
        StringEncoder stringEncoder = soundex.getStringEncoder();
        assertEquals("S530", stringEncoder.encode("Smith"));
        assertEquals("J250", stringEncoder.encode("Johnson"));
        assertEquals("T522", stringEncoder.encode("Turing"));
    }

    @Test
    public void testConstructors() {
        Soundex soundex = new Soundex();
        Soundex soundex1 = new Soundex(Soundex.US_ENGLISH);
        Soundex soundex2 = new Soundex("01230120022455012623010202");
        Soundex soundex3 = new Soundex("01230120022455012623010202".toCharArray());
    }
}