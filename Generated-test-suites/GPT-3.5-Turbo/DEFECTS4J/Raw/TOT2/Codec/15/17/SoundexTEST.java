import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundexConstructor() {
        Soundex soundex = new Soundex();
        assertNotNull(soundex);
    }

    @Test
    public void testSoundexEncoding() {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex("TestString");
        assertEquals("T235", encoded);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Test", "Testt");
        assertEquals(4, difference);
    }

    @Test
    public void testSoundexMappingCodeWithValidChar() {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.getMappingCode("TestString", 2);
        assertEquals('5', mappingCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexMappingCodeWithInvalidChar() {
        Soundex soundex = new Soundex();
        char mappingCode = soundex.getMappingCode("TestString", 10); // Invalid index
    }

    @Test
    public void testEncodeWithStringInput() throws EncoderException {
        Soundex soundex = new Soundex();
        String encoded = (String) soundex.encode("Test");
        assertEquals("T23", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123); // Non-string input, should throw exception
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Soundex soundex = new Soundex();
        Object encoded = soundex.encode("Test");
        assertTrue(encoded instanceof String);
        assertEquals("T23", encoded);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundexWithEmptyString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex("");
        assertEquals("", encoded);
    }

    @Test
    public void testSoundexWithNullString() {
        Soundex soundex = new Soundex();
        String encoded = soundex.soundex(null);
        assertNull(encoded);
    }

}