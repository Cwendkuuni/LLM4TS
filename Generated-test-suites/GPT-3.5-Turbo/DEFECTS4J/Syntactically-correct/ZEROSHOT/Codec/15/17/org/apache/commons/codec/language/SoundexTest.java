import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Before;
import org.junit.Test;

public class SoundexTest {
    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    @Test
    public void testEncode() {
        assertEquals("A123", soundex.encode("Andrew"));
        assertEquals("B343", soundex.encode("Brown"));
        assertEquals("C340", soundex.encode("Clark"));
        assertEquals("D250", soundex.encode("Davis"));
        assertEquals("S365", soundex.encode("Smith"));
    }

    @Test
    public void testEncodeEmptyString() {
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncodeNullString() {
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() {
        try {
            assertEquals(2, soundex.difference("Andrew", "Andres"));
            assertEquals(4, soundex.difference("Brown", "Brawn"));
            assertEquals(2, soundex.difference("Clark", "Clarke"));
            assertEquals(3, soundex.difference("Davis", "Davies"));
            assertEquals(3, soundex.difference("Smith", "Smyth"));
        } catch (EncoderException e) {
            fail("EncoderException thrown");
        }
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceInvalidString() throws EncoderException {
        soundex.difference("123", "456");
    }

    @Test
    public void testGetMaxLength() {
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSetMaxLength() {
        soundex.setMaxLength(6);
        assertEquals(6, soundex.getMaxLength());
    }
}