import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("hello", "holla");
        assertEquals(2, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithInvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("hello", 123);
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        String result = soundex.encode("hello");
        assertEquals("H400", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidInput() throws EncoderException {
        Soundex soundex = new Soundex();
        String result = soundex.encode(123);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("hello");
        assertEquals("H400", result);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int result = soundex.getMaxLength();
        assertEquals(4, result);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(3);
        int result = soundex.getMaxLength();
        assertEquals(3, result);
    }

}