import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("Test");
        assertEquals("T230", encoded);
    }

    @Test
    public void testEncodeWithEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode("");
        assertEquals("", encoded);
    }

    @Test
    public void testEncodeWithNullString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.encode(null);
        assertNull(encoded);
    }

    @Test
    public void testEncodeWithInvalidCharacter() {
        Soundex soundex = new Soundex();
        try {
            String encoded = soundex.encode("Tes%t");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testDifference() throws Exception {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Test", "Tast");
        assertEquals(3, difference);
    }

    @Test
    public void testDifferenceWithEmptyStrings() throws Exception {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("", "");
        assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidObject() throws Exception {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testSoundex() throws Exception {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex("Test");
        assertEquals("T230", encoded);
    }
}