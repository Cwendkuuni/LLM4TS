import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("A000", soundex.encode("Ash"));
        assertEquals("A200", soundex.encode("Ashley"));
        assertEquals("B400", soundex.encode("Brian"));
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Ash", "Ash"));
        assertEquals(2, soundex.difference("Ash", "Ashley"));
        assertEquals(1, soundex.difference("Ash", "Bash"));
        assertEquals(0, soundex.difference("Ash", "Brian"));
    }
    
    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }
    
    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertEquals('0', mapping[0]);
        assertEquals('1', mapping[1]);
        assertEquals('2', mapping[2]);
        assertEquals('3', mapping[3]);
        assertEquals('0', mapping[4]);
        assertEquals('1', mapping[5]);
        assertEquals('2', mapping[6]);
        assertEquals('0', mapping[7]);
        assertEquals('2', mapping[22]);
    }
    
    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A200", soundex.soundex("Ashley"));
        assertEquals("B400", soundex.soundex("Brian"));
        assertNull(soundex.soundex(null));
    }
}