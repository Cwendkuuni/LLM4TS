import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructor1() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructor2() {
        char[] mapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructor3() {
        String mapping = "0123456789";
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            Object result = soundex.encode("Test");
            assertTrue(result instanceof String);
        } catch (EncoderException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Test");
        assertEquals("T230", result);
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            int result = soundex.difference("Test", "Toast");
            assertEquals(2, result);
        } catch (EncoderException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSoundexMappingCode() {
        Soundex soundex = new Soundex();
        try {
            char result = soundex.map('A');
            assertEquals('0', result);
        } catch (IllegalArgumentException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSoundexMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundexSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
    }

    @Test
    public void testSoundexSoundexString() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Hello");
        assertEquals("H400", result);
    }
}