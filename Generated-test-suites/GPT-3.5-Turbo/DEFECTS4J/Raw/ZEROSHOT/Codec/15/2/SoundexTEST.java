import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        assertEquals("A261", soundex.encode("Andrew"));
        assertEquals("B210", soundex.encode("Brian"));
        assertEquals("C153", soundex.encode("Charles"));
        assertEquals("D250", soundex.encode("David"));
    }

    @Test
    public void testDifference() throws Exception {
        assertEquals(0, soundex.difference("Andrew", "ANN"));
        assertEquals(2, soundex.difference("Brian", "Bren"));
        assertEquals(3, soundex.difference("Charles", "Carlos"));
        assertEquals(1, soundex.difference("David", "Dave"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeNonString() {
        soundex.encode(123);
    }

    @Test
    public void testEncodeEmptyString() {
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testSoundex() {
        assertEquals("A261", soundex.soundex("Andrew"));
        assertEquals("B210", soundex.soundex("Brian"));
        assertEquals("C153", soundex.soundex("Charles"));
        assertEquals("D250", soundex.soundex("David"));
    }
}