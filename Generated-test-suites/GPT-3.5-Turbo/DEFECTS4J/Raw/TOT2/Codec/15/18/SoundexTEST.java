import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "Test";
        String s2 = "Tess";
        int result = soundex.difference(s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "Hello";
        Object result = soundex.encode(input);
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "World";
        String result = soundex.encode(input);
        assertNotNull(result);
        assertEquals("W643", result);
    }

    @Test
    public void testSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
        assertEquals(US_ENGLISH_MAPPING_STRING, new String(mapping));
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        int maxLength = soundex.getMaxLength();
        assertEquals(5, maxLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapIllegalArgumentException() {
        Soundex soundex = new Soundex();
        soundex.map('!');
    }

    @Test
    public void testSoundexMethod() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("California");
        assertNotNull(result);
        assertEquals("C416", result);
    }

}