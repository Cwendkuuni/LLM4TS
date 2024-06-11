import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("hello", "hallo"));
        assertEquals(2, soundex.difference("test", "taste"));
        assertEquals(0, soundex.difference("abc", "def"));
    }
    
    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("H200", soundex.encode("hello"));
        assertEquals("T230", soundex.encode("test"));
    }
    
    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        assertEquals("H200", soundex.encode("hello"));
        assertEquals("T230", soundex.encode("test"));
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("H200", soundex.soundex("hello"));
        assertEquals("T230", soundex.soundex("test"));
    }
    
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        assertArrayEquals(Soundex.US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(6);
        assertEquals(6, soundex.getMaxLength());
    }
}