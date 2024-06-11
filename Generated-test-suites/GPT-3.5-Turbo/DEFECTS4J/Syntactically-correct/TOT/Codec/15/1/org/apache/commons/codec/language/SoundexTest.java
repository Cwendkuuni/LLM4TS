
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SoundexTest {

    private Soundex soundex;

    @Before
    public void setUp() {
        soundex = new Soundex();
    }

    // Test cases for encode(String) method

    @Test
    public void testEncode_shouldReturnSoundexCode() {
        String code = soundex.encode("Apache");
        assertEquals("A120", code);
    }

    @Test
    public void testEncode_shouldHandleEmptyString() {
        String code = soundex.encode("");
        assertEquals("", code);
    }

    @Test
    public void testEncode_shouldHandleNullString() {
        String code = soundex.encode(null);
        assertNull(code);
    }

    // Test cases for encode(Object) method

    @Test
    public void testEncodeObject_shouldReturnSoundexCode() throws EncoderException {
        String code = (String) soundex.encode((Object) "Apache");
        assertEquals("A120", code);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeObject_shouldThrowEncoderExceptionForNonStringParameter() throws EncoderException {
        soundex.encode((Object) 123);
    }

    // Test cases for difference(String, String) method

    @Test
    public void testDifference_shouldReturnNumberOfMatchingCharacters() throws EncoderException {
        int difference = soundex.difference("Apache", "Apollo");
        assertEquals(1, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_shouldThrowEncoderExceptionForNullFirstString() throws EncoderException {
        soundex.difference(null, "Apollo");
    }

    @Test(expected = EncoderException.class)
    public void testDifference_shouldThrowEncoderExceptionForNullSecondString() throws EncoderException {
        soundex.difference("Apache", null);
    }

    // Test cases for getMappingCode(String, int) method

    @Test
    public void testGetMappingCode_shouldReturnMappingCode() {
        char code = soundex.getMappingCode("Apache", 1);
        assertEquals('1', code);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMappingCode_shouldThrowIllegalArgumentExceptionForInvalidCharacter() {
        soundex.getMappingCode("Apache", 6);
    }

    // Test cases for getMaxLength() method

    @Test
    public void testGetMaxLength_shouldReturnMaxLength() {
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    // Test cases for setMaxLength(int) method

    @Test
    public void testSetMaxLength_shouldSetMaxLength() {
        soundex.setMaxLength(3);
        int maxLength = soundex.getMaxLength();
        assertEquals(3, maxLength);
    }

    // Test cases for soundex(String) method

    @Test
    public void testSoundex_shouldReturnSoundexCode() {
        String code = soundex.soundex("Apache");
        assertEquals("A120", code);
    }

    @Test
    public void testSoundex_shouldHandleEmptyString() {
        String code = soundex.soundex("");
        assertEquals("", code);
    }

    @Test
    public void testSoundex_shouldHandleNullString() {
        String code = soundex.soundex(null);
        assertNull(code);
    }

}

