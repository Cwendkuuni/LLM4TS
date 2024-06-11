import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex = new Soundex();

    @Test
    public void testEncode() {
        assertEquals("T235", soundex.encode("Test"));
    }

    @Test
    public void testEncodeEmptyString() {
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncodeNullString() {
        assertNull(soundex.encode(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidType() throws EncoderException {
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, soundex.difference("Hello", "Hello"));
        assertEquals(2, soundex.difference("Luke", "Duke"));
        assertEquals(0, soundex.difference("Hello", "Bye"));
    }

    @Test
    public void testDifferenceNullString() throws EncoderException {
        assertEquals(0, soundex.difference(null, "Hello"));
        assertEquals(0, soundex.difference("Hello", null));
        assertEquals(0, soundex.difference(null, null));
    }
    
    @Test
    public void testSoundex() {
        assertEquals("T235", soundex.soundex("Test"));
        assertEquals("L200", soundex.soundex("Luke"));
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void testSoundexNullString() {
        assertNull(soundex.soundex(null));
    }
}