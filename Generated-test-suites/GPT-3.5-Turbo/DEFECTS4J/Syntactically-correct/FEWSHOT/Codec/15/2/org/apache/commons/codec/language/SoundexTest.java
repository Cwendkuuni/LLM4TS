import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Robert");
        assertEquals("R163", result);
    }

    @Test
    public void testEncodeEmptyString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("");
        assertEquals("", result);
    }

    @Test
    public void testEncodeNullString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode(null);
        assertNull(result);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Robert", "Rupert");
        assertEquals(2, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceInvalidObjects() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(new Integer(10), new Integer(20));
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int result = soundex.getMaxLength();
        assertEquals(4, result);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        int result = soundex.getMaxLength();
        assertEquals(5, result);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Robert");
        assertEquals("R163", result);
    }

    @Test
    public void testSoundexEmptyString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("");
        assertEquals("", result);
    }

    @Test
    public void testSoundexNullString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex(null);
        assertNull(result);
    }

    @Test
    public void testSoundexWithDigits() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("S1234");
        assertEquals("S134", result);
    }

    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] result = soundex.getSoundexMapping();
        assertEquals("01230120022455012623010202", new String(result));
    }

    @Test
    public void testSoundexWithCustomMapping() {
        char[] mapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(mapping);
        String result = soundex.soundex("Robert");
        assertEquals("80190103", result);
    }
}