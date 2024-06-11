import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SoundexTest {

    @Test
    public void testSoundexConstructor_US_ENGLISH() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructor_CustomMapping() {
        char[] customMapping = {'0','1','2','3','4','5','6','7','8','9'};
        Soundex soundex = new Soundex(customMapping);
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructor_InvalidMapping() {
        try {
            Soundex soundex = new Soundex("abc");
            fail("Should have thrown IllegalArgumentException for invalid mapping");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("hello", "hallo");
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexEncode_ObjectType() throws EncoderException {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("hello");
        assertNotNull(result);
        assertEquals("H440", result);
    }

    @Test
    public void testSoundexEncode_StringType() throws EncoderException {
        Soundex soundex = new Soundex();
        String result = soundex.encode("halo");
        assertNotNull(result);
        assertEquals("H400", result);
    }

    @Test
    public void testSoundexSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("hello");
        assertNotNull(result);
        assertEquals("H400", result);
    }

}