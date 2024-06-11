import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    Soundex soundex = new Soundex();

    @Test
    public void testEncode_objectOfStringType() throws EncoderException {
        Object obj = "Apache";
        Object expected = "A120";
        assertEquals(expected, soundex.encode(obj));
    }

    @Test
    public void testEncode_emptyString() {
        String str = "";
        String expected = "";
        assertEquals(expected, soundex.encode(str));
    }

    @Test
    public void testEncode_validString() {
        String str = "Hello";
        String expected = "H400";
        assertEquals(expected, soundex.encode(str));
    }

    @Test(expected = EncoderException.class)
    public void testEncode_objectOfNonStringType() throws EncoderException {
        Object obj = 123;
        soundex.encode(obj);
    }

    @Test
    public void testDifference_sameStrings() throws EncoderException {
        String s1 = "Hello";
        String s2 = "Hello";
        int expected = 4;
        assertEquals(expected, soundex.difference(s1, s2));
    }

    @Test
    public void testDifference_differentStrings() throws EncoderException {
        String s1 = "Hello";
        String s2 = "World";
        int expected = 0;
        assertEquals(expected, soundex.difference(s1, s2));
    }

    @Test(expected = EncoderException.class)
    public void testDifference_nonStringInput() throws EncoderException {
        soundex.difference(123, "Hello");
    }

    @Test
    public void testSoundex_nullString() {
        String str = null;
        assertNull(soundex.soundex(str));
    }

    @Test
    public void testSoundex_emptyString() {
        String str = "";
        String expected = "";
        assertEquals(expected, soundex.soundex(str));
    }

    @Test
    public void testSoundex_validString() {
        String str = "Hello";
        String expected = "H400";
        assertEquals(expected, soundex.soundex(str));
    }
}