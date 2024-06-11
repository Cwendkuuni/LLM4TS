import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexTest {

    Soundex soundex = new Soundex();

    @Test
    public void testSoundexEncode() {
        String encoded = soundex.encode("Test");
        assertEquals("T230", encoded);
    }

    @Test
    public void testSoundexDifference() throws EncoderException {
        int difference = soundex.difference("Test", "Taste");
        assertEquals(3, difference);
    }

    @Test
    public void testSoundexMaxLength() {
        // Deprecated method, should throw UnsupportedOperationException
        try {
            soundex.setMaxLength(5);
        } catch (UnsupportedOperationException e) {
            assertEquals("This feature is not needed since the encoding size must be constant. Will be removed in 2.0.", e.getMessage());
        }
    }

    @Test
    public void testSoundexMappingCode() {
        char mappingCode = soundex.getMappingCode("Test", 0);
        assertEquals('T', mappingCode);
    }

    @Test
    public void testSoundexEncodeObject() throws EncoderException {
        Object encodedObject = soundex.encode("Test");
        assertEquals("T230", encodedObject);
    }

    @Test
    public void testInvalidSoundexEncodeObject() {
        // Should throw EncoderException for non-string object
        try {
            soundex.encode(123);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Soundex encode is not of type java.lang.String", e.getMessage());
        }
    }

}