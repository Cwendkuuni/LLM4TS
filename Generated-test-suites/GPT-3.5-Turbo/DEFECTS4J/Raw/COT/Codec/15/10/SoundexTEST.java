import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_Object() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Apache");
        assertEquals("A120", encoded);
    }

    @Test
    public void testEncode_String() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Commons");
        assertEquals("C552", encoded);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Test", "Text");
        assertEquals(3, difference);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_ObjectWithWrongType() throws EncoderException {
        Soundex soundex = new Soundex();
        Integer encoded = (Integer) soundex.encode(123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_StringWithInvalidCharacter() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Apache!");
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex("Tester");
        assertEquals("T236", encoded);
    }

}