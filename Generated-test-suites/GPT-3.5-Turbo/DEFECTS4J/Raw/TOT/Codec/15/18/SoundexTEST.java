import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexTest {
    private static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    private static final char[] US_ENGLISH_MAPPING = US_ENGLISH_MAPPING_STRING.toCharArray();
    private static final Soundex SOUNDEX = new Soundex();

    @Test
    public void testSoundex_DefaultConstructor() {
        assertNotNull(SOUNDEX);
    }

    @Test
    public void testSoundex_CharArrayConstructor() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING);
        assertNotNull(soundex);
        assertEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }
    
    @Test
    public void testSoundex_StringConstructor() {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING);
        assertNotNull(soundex);
        assertEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    @Test
    public void testEncode_ObjectTypeOfString() throws EncoderException {
        String encoded = (String) SOUNDEX.encode("Apache");
        assertEquals("A120", encoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_ObjectTypeNotString() throws EncoderException {
        SOUNDEX.encode(123);
    }

    @Test
    public void testEncode_String() {
        String encoded = SOUNDEX.encode("Apache");
        assertEquals("A120", encoded);
    }

    @Test
    public void testEncode_NullString() {
        String encoded = SOUNDEX.encode(null);
        assertEquals(null, encoded);
    }

    @Test
    public void testEncode_EmptyString() {
        String encoded = SOUNDEX.encode("");
        assertEquals("", encoded);
    }

    @Test
    public void testDifference_StringsSame() throws EncoderException {
        int difference = SOUNDEX.difference("Apache", "Apache");
        assertEquals(4, difference);
    }

    @Test
    public void testDifference_StringsDifferent() throws EncoderException {
        int difference = SOUNDEX.difference("Apache", "Apples");
        assertEquals(2, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_NullString1() throws EncoderException {
        SOUNDEX.difference(null, "Apache");
    }

    @Test(expected = EncoderException.class)
    public void testDifference_NullString2() throws EncoderException {
        SOUNDEX.difference("Apache", null);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_NullStrings() throws EncoderException {
        SOUNDEX.difference(null, null);
    }
}