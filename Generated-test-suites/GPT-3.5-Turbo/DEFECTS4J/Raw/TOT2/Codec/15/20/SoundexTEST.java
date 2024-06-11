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
    public void testDifference() throws EncoderException {
        assertEquals(0, soundex.difference("hello", "world"));
        assertEquals(2, soundex.difference("hello", "hallo"));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        assertEquals("H460", soundex.encode("hello"));
    }

    @Test
    public void testEncodeString() {
        assertEquals("H460", soundex.encode("hello"));
    }

    @Test
    public void testGetMappingCode() {
        assertEquals('0', soundex.getMappingCode("hello", 0));
        assertEquals('H', soundex.getMappingCode("hello", 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCodeException() {
        soundex.getMappingCode("hello", 8);
    }

    @Test
    public void testSoundex() {
        assertEquals("H460", soundex.soundex("hello"));
    }

    @Test
    public void testSoundexMaxLength() {
        soundex.setMaxLength(3);
        assertEquals("H46", soundex.soundex("hello"));
    }

}