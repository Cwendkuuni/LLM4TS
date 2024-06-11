import org.junit.Test;
import org.apache.commons.codec.EncoderException;

public class SoundexTest {

    private Soundex soundex;

    @Test
    public void testEncode_Object_ValidInput() throws EncoderException {
        soundex = new Soundex();
        Object obj = "TestString";
        String encoded = soundex.encode(obj);
        assertEquals("T232", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_Object_InvalidInput() throws EncoderException {
        soundex = new Soundex();
        Object obj = 12345;
        soundex.encode(obj);
    }

    @Test
    public void testEncode_String_ValidInput() {
        soundex = new Soundex();
        String str = "TestString";
        String encoded = soundex.encode(str);
        assertEquals("T232", encoded);
    }

    @Test
    public void testEncode_String_ValidInput_EmptyString() {
        soundex = new Soundex();
        String str = "";
        String encoded = soundex.encode(str);
        assertEquals("", encoded);
    }

    @Test
    public void testDifference_ValidInput() throws EncoderException {
        soundex = new Soundex();
        String s1 = "TestString";
        String s2 = "TesstSring";
        int difference = soundex.difference(s1, s2);
        assertEquals(2, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_InvalidInput() throws EncoderException {
        soundex = new Soundex();
        Object s1 = 12345;
        String s2 = "TesstSring";
        soundex.difference(s1, s2);
    }

    @Test
    public void testSoundex_ValidInput() {
        soundex = new Soundex();
        String str = "TestString";
        String encoded = soundex.soundex(str);
        assertEquals("T232", encoded);
    }

    @Test
    public void testSoundex_ValidInput_EmptyString() {
        soundex = new Soundex();
        String str = "";
        String encoded = soundex.soundex(str);
        assertEquals("", encoded);
    }
}