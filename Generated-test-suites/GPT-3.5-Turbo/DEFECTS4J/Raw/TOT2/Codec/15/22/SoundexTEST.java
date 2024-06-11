import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructorDefaultMapping() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructorCustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexConstructorCustomMappingString() {
        String customMappingString = "0123456789";
        Soundex soundex = new Soundex(customMappingString);
        assertNotNull(soundex);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "Hello";
        String s2 = "Halo";
        int result = soundex.difference(s1, s2);
        assertEquals(3, result);
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        String input = "Test";
        Object result = soundex.encode(input);
        assertEquals("T230", result);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String input = "Test";
        String result = soundex.encode(input);
        assertEquals("T230", result);
    }

    @Test
    public void testSoundexMethodWithNull() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex(null);
        assertNull(result);
    }

    @Test
    public void testSoundexMethodWithEmptyString() {
        Soundex soundex = new Soundex();
        String input = "";
        String result = soundex.soundex(input);
        assertEquals("0000", result);
    }

    @Test
    public void testSoundexMethod() {
        Soundex soundex = new Soundex();
        String input = "Soundex";
        String result = soundex.soundex(input);
        assertEquals("S532", result);
    }
}