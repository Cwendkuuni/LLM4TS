import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testDefaultConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }
    
    @Test
    public void testCustomMappingConstructor() {
        char[] mapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }
    
    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object result = soundex.encode("Test");
        assertTrue(result instanceof String);
        assertEquals("T230", result);
    }
    
    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String result = soundex.encode("Test");
        assertEquals("T230", result);
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int result = soundex.difference("Test", "Test");
        assertEquals(4, result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeObjectException() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(new Object());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEncodeStringException() {
        Soundex soundex = new Soundex();
        soundex.encode(null);
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        String result = soundex.soundex("Test");
        assertEquals("T230", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCodeException() {
        Soundex soundex = new Soundex();
        soundex.getMappingCode("Test", 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMapException() {
        Soundex soundex = new Soundex();
        soundex.map('!');
    }
}