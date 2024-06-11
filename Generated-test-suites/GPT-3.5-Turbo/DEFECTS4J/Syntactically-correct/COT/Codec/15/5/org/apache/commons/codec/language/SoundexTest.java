import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("", ""));
        assertEquals(4, soundex.difference("John", "Jane"));
        assertEquals(2, soundex.difference("Robert", "Rupert"));
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("J250", soundex.encode("John"));
        assertEquals("R163", soundex.encode("Robert"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("J250", soundex.soundex("John"));
        assertEquals("R163", soundex.soundex("Robert"));
    }
    
    @Test
    public void testSoundexWithSpecialCharacters() {
        Soundex soundex = new Soundex();
        assertEquals("T200", soundex.soundex("T$#@%"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSoundexWithInvalidCharacter() {
        Soundex soundex = new Soundex();
        soundex.soundex("abc");
    }
    
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }
    
    // Add additional test cases for potential edge cases and exception handling scenarios
    
}

