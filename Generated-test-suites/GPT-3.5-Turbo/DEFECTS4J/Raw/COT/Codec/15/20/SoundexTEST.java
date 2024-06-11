
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertNotNull(soundex.encode("Test"));
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        assertEquals("T200", soundex.encode("Test"));
        assertEquals("", soundex.encode(""));
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Test", "Test"));
        assertEquals(1, soundex.difference("Test", "Taste"));
        assertEquals(2, soundex.difference("Test", "Toss"));
        assertEquals(0, soundex.difference("Test", ""));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectWithInvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSoundexMappingWithInvalidCharacter() {
        new Soundex("0123012002245501262301020?");
    }
    
    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex("0123012002245501262301020");
        assertEquals("0212", soundex.encode("Test"));
    }
}