import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertEquals("A261", soundex.encode("Andrew"));
    }

    @Test
    public void testSoundexCustomMappingConstructor() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("0285", soundex.encode("Hello"));
    }

    @Test
    public void testSoundexStringMappingConstructor() {
        Soundex soundex = new Soundex("00000000001111111111222");
        assertEquals("0121", soundex.encode("Testing"));
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Hello", "Halo");
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("Hello");
        assertEquals("H400", result.toString());
    }

    @Test
    public void testSoundexEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Hello");
        assertEquals("H400", result);
    }

    @Test
    public void testSoundexGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSoundexSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundexSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
    }

    @Test
    public void testSoundexSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Hello");
        assertEquals("H400", result);
    }
}