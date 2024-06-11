import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.soundex("John"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(2, soundex.difference("John", "Jane"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("John"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(new Object());
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

}