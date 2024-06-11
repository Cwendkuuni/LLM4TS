import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("K530", soundex.soundex("cat"));
        assertEquals("G200", soundex.soundex("dog"));
        assertEquals("F320", soundex.soundex("fish"));
        assertEquals("S530", soundex.soundex("snake"));
        assertNull(soundex.soundex(null));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("F320", soundex.encode("fish"));
        assertEquals("D200", soundex.encode("dog"));
        assertEquals("A234", soundex.encode("apple"));
        assertEquals("L200", soundex.encode("lion"));
        assertNull(soundex.encode(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("apple", "aple"));
        assertEquals(3, soundex.difference("dog", "doll"));
        assertEquals(2, soundex.difference("cat", "rat"));
        assertEquals(1, soundex.difference("fish", "wish"));
        assertEquals(0, soundex.difference("snake", "cake"));
    }
    
    @Test(expected = EncoderException.class)
    public void testDifferenceWithNonStringInputs() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(123, "dog");
    }
}