import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "Test";
        String s2 = "Tust";
        int difference = soundex.difference(s1, s2);
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object obj = "Hello";
        Object result = soundex.encode(obj);
        assertNotNull(result);
    }

    @Test
    public void testSoundexEncodeString() {
        Soundex soundex = new Soundex();
        String input = "World";
        String result = soundex.encode(input);
        assertEquals("W643", result);
    }

    @Test
    public void testSoundexEncodeInvalidInput() {
        Soundex soundex = new Soundex();
        try {
            soundex.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSoundexSoundex() {
        Soundex soundex = new Soundex();
        String input = "Test";
        String result = soundex.soundex(input);
        assertEquals("T230", result);
    }

    @Test
    public void testSoundexSoundexWithNullInput() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex(null);
        assertNull(result);
    }

}