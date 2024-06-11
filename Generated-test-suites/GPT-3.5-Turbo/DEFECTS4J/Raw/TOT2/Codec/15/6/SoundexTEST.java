import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexMappingConstructor() {
        char[] mapping = {'0', '1', '2', '3', '4', '5', '6'};
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexStringMappingConstructor() {
        String mapping = "0123456";
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("test", "hello"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("T200", soundex.encode("test"));
    }

    @Test
    public void testEncodeString() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("T200", soundex.encode("test"));
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapIllegalArgumentException() {
        Soundex soundex = new Soundex();
        soundex.map('Z');
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("T200", soundex.soundex("test"));
    }

}