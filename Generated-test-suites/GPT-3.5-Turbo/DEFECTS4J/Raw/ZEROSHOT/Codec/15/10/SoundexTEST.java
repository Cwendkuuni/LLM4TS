import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        assertEquals("A123", soundex.encode("Apple"));
        assertEquals("B600", soundex.encode("Banana"));
        assertEquals("C400", soundex.encode("Cherry"));
        assertEquals("D650", soundex.encode("Durian"));
        assertEquals("E260", soundex.encode("Elderberry"));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, soundex.difference("Apple", "Apple"));
        assertEquals(4, soundex.difference("Banana", "Banana"));
        assertEquals(4, soundex.difference("Cherry", "Cherry"));
        assertEquals(4, soundex.difference("Durian", "Durian"));
        assertEquals(4, soundex.difference("Elderberry", "Elderberry"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() throws EncoderException {
        soundex.encode(123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexMappingCodeWithInvalidCharacter() {
        soundex.soundex("Apple!");
    }

    @Test
    public void testSoundexMappingCodeWithValidCharacter() {
        assertEquals("A123", soundex.soundex("Apple"));
        assertEquals("B600", soundex.soundex("Banana"));
        assertEquals("C400", soundex.soundex("Cherry"));
        assertEquals("D650", soundex.soundex("Durian"));
        assertEquals("E260", soundex.soundex("Elderberry"));
    }

}