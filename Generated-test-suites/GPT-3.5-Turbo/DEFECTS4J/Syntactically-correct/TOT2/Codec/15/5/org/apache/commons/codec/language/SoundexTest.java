import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.soundex("Apple"));
    }

    @Test
    public void testSoundexCustomMappingConstructor() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        assertEquals("0123", soundex.soundex("Apple"));
    }

    @Test
    public void testSoundexStringMappingConstructor() {
        Soundex soundex = new Soundex("0123");
        assertEquals("0123", soundex.soundex("Apple"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("Apple"));
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("Apple"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Apple", "Aplee"));
    }

}