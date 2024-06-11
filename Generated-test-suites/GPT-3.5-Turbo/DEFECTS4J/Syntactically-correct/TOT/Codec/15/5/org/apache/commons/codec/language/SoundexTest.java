import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Test
    public void testConstructor1() {
        soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testConstructor2() {
        char[] mapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testConstructor3() {
        String mapping = "0123456789";
        soundex = new Soundex(mapping);
        assertNotNull(soundex);
    }

    @Test
    public void testEncode1() {
        soundex = new Soundex();
        String encoded = soundex.encode("Test");
        assertEquals("T230", encoded);
    }

    @Test
    public void testEncode2() {
        soundex = new Soundex();
        String encoded = soundex.encode("AnotherTest");
        assertEquals("A5363", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncode3() {
        soundex = new Soundex();
        Object obj = new Object();
        soundex.encode(obj);
    }

    @Test
    public void testDifference1() throws EncoderException {
        soundex = new Soundex();
        int difference = soundex.difference("Test", "Test");
        assertEquals(4, difference);
    }

    @Test
    public void testDifference2() throws EncoderException {
        soundex = new Soundex();
        int difference = soundex.difference("Test", "Tset");
        assertEquals(3, difference);
    }

    @Test
    public void testDifference3() throws EncoderException {
        soundex = new Soundex();
        int difference = soundex.difference("Test", "Best");
        assertEquals(2, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference4() throws EncoderException {
        soundex = new Soundex();
        soundex.difference("Test", new Object());
    }

    @Test
    public void testMaxLength() {
        soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    @Test
    public void testSoundexMapping() {
        soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertNotNull(mapping);
    }

    @Test
    public void testSoundex1() {
        soundex = new Soundex();
        String encoded = soundex.soundex("Test");
        assertEquals("T230", encoded);
    }

    @Test
    public void testSoundex2() {
        soundex = new Soundex();
        String encoded = soundex.soundex("AnotherTest");
        assertEquals("A5363", encoded);
    }

    @Test
    public void testSoundex3() {
        soundex = new Soundex();
        String encoded = soundex.soundex("");
        assertEquals("", encoded);
    }

    @Test
    public void testSoundex4() {
        soundex = new Soundex();
        String encoded = soundex.soundex("T");
        assertEquals("T000", encoded);
    }

}