import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            assertEquals(0, soundex.difference("hello", "world"));
            assertEquals(4, soundex.difference("hello", "hello"));
            assertEquals(2, soundex.difference("java", "jave"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testEncode_Object() {
        Soundex soundex = new Soundex();
        try {
            assertEquals("H342", soundex.encode((Object)"Hello"));
        } catch (EncoderException e) {
            fail("EncoderException should not be thrown");
        }
    }

    @Test
    public void testEncode_String() {
        Soundex soundex = new Soundex();
        assertEquals("H342", soundex.encode("Hello"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("H342", soundex.soundex("Hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_Object_exception() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode((Object)123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_String_exception() {
        Soundex soundex = new Soundex();
        soundex.encode("123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCode_exception() {
        Soundex soundex = new Soundex();
        soundex.getMappingCode("A", 1);
    }

}