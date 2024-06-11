import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("John", "John");
        assertEquals("Difference should be 4", 4, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithInvalidString() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference("John", 123);
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("John");
        assertEquals("Encoded value should be 'J500'", "J500", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithInvalidObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("John");
        assertEquals("Encoded value should be 'J500'", "J500", result);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int result = soundex.getMaxLength();
        assertEquals("Max length should be 4", 4, result);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(6);
        int result = soundex.getMaxLength();
        assertEquals("Max length should be 6", 6, result);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("John");
        assertEquals("Soundex value should be 'J500'", "J500", result);
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("");
        assertEquals("Soundex value should be ''", "", result);
    }

    @Test
    public void testSoundexWithCleanString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("John");
        assertEquals("Soundex value should be 'J500'", "J500", result);
    }
}