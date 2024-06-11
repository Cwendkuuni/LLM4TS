import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        assertEquals("T200", soundex.encode("Test"));
        assertEquals("B223", soundex.encode("Blue"));
        assertEquals("W000", soundex.encode("White"));
        assertEquals("S531", soundex.encode("Soundex"));
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();

        assertEquals(0, soundex.difference("Test", "Best"));
        assertEquals(1, soundex.difference("Blue", "Grew"));
        assertEquals(2, soundex.difference("White", "Wide"));
        assertEquals(3, soundex.difference("Soundex", "Sandwich"));
    }
}