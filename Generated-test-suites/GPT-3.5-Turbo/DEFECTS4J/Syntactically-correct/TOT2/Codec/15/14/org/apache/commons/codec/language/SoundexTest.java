import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testSoundexConstructorDefaultMapping() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }
    
    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String s1 = "hello";
        String s2 = "halo";
        int difference = soundex.difference(s1, s2);
        assertEquals(3, difference);
    }
    
    @Test
    public void testSoundexEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object obj = "test";
        Object result = soundex.encode(obj);
        assertNotNull(result);
        assertEquals("T230", result);
    }
    
    @Test
    public void testSoundexEncodeString() {
        Soundex soundex = new Soundex();
        String str = "testing";
        String result = soundex.encode(str);
        assertEquals("T235", result);
    }
    
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
        assertEquals(US_ENGLISH_MAPPING_STRING.length(), mapping.length);
    }
    
    @Test
    public void testSoundexSoundex() {
        Soundex soundex = new Soundex();
        String input = "abc123!@#";
        String result = soundex.soundex(input);
        assertEquals("A120", result);
    }
}