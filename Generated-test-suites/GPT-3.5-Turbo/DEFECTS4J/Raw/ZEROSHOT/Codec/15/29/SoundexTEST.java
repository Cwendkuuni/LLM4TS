import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Test", "Rest"));
        assertEquals(1, soundex.difference("Hello", "Halo"));
        assertEquals(2, soundex.difference("Java", "Ruby"));
        assertEquals(3, soundex.difference("Soundex", "Sound"));
        assertEquals(4, soundex.difference("Apache", "Apple"));
    }
    
    @Test(expected = EncoderException.class)
    public void testDifferenceNonStringObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(123, "Test");
    }
    
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("T530", soundex.encode("Test"));
        assertEquals("H400", soundex.encode("Hello"));
        assertEquals("J100", soundex.encode("Java"));
        assertEquals("S532", soundex.encode("Soundex"));
        assertEquals("A102", soundex.encode("Apache"));
    }
    
    @Test
    public void testEncodeNonStringObject() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("true", soundex.encode(true));
        assertEquals("123", soundex.encode(123));
        assertEquals("12.34", soundex.encode(12.34));
    }
    
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] expectedMapping = {'0', '1', '2', '3', '0', '1', '2', '0', '0', '2', '2', '4', '5', '5', '0', '1', '2', '6', '2', '3', '0', '1', '0', '2', '0', '2'};
        assertArrayEquals(expectedMapping, soundex.getSoundexMapping());
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(3);
        assertEquals(3, soundex.getMaxLength());
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("T530", soundex.soundex("Test"));
        assertEquals("H400", soundex.soundex("Hello"));
        assertEquals("J100", soundex.soundex("Java"));
        assertEquals("S532", soundex.soundex("Soundex"));
        assertEquals("A102", soundex.soundex("Apache"));
    }
    
    @Test
    public void testSoundexNullInput() {
        Soundex soundex = new Soundex();
        assertNull(soundex.soundex(null));
    }
    
    @Test
    public void testSoundexEmptyInput() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.soundex(""));
    }
    
}