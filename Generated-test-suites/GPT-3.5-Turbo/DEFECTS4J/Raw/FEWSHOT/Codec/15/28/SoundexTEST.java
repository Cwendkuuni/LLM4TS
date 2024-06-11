import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("T522", soundex.encode("Thomas"));
        assertEquals("A116", soundex.encode("Andrew"));
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        
        // Same String
        assertEquals(4, soundex.difference("Smith", "Smith"));
        
        // Different Strings
        assertEquals(1, soundex.difference("Smith", "Smyth"));
        assertEquals(2, soundex.difference("Thomas", "Thomson"));
        assertEquals(3, soundex.difference("Andrew", "Andreu"));
        assertEquals(0, soundex.difference("Smith", "Jones"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("T522", soundex.soundex("Thomas"));
        assertEquals("A116", soundex.soundex("Andrew"));
    }
}