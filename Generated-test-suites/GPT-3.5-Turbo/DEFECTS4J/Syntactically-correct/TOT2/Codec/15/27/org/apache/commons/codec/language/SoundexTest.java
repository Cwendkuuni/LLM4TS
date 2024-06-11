import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testSoundexUS_ENGLISH() {
        assertEquals("T600", soundex.soundex("Test"));
    }

    @Test
    public void testSoundexCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex customSoundex = new Soundex(customMapping);
        assertEquals("2345", customSoundex.soundex("Test"));
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        assertEquals(4, soundex.difference("Test", "Test"));
        assertEquals(0, soundex.difference("Test", "XYZ"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObjectException() throws EncoderException {
        soundex.encode(new Object());
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        assertEquals("T600", soundex.encode("Test"));
    }

    @Test
    public void testEncodeString() {
        assertEquals("T600", soundex.encode("Test"));
    }

    @Test
    public void testSetMaxLength() {
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundexMaxLength() {
        soundex.setMaxLength(5);
        assertEquals("T6000", soundex.soundex("Test"));
    }

    @Test
    public void testSoundexEmptyString() {
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void testSoundexNullString() {
        assertNull(soundex.soundex(null));
    }

}