
import org.junit.Test;
import org.apache.commons.codec.EncoderException;

import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        try {
            assertEquals("H452", soundex.encode("Hello"));
        } catch (EncoderException e) {
            fail("EncoderException thrown");
        }
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        assertEquals("H452", soundex.encode("Hello"));
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            assertEquals(2, soundex.difference("Hello", "Hilco"));
        } catch (EncoderException e) {
            fail("EncoderException thrown");
        }
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
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
        assertEquals("H452", soundex.soundex("Hello"));
    }

}
