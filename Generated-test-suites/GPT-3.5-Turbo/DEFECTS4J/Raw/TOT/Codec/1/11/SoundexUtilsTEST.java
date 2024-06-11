import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    private SoundexUtils soundexUtils;

    @Before
    public void setUp() {
        soundexUtils = new SoundexUtils();
    }

    // Test for the clean() method

    @Test
    public void clean_NullString_ReturnNull() {
        assertNull(soundexUtils.clean(null));
    }

    @Test
    public void clean_EmptyString_ReturnEmptyString() {
        assertEquals("", soundexUtils.clean(""));
    }

    @Test
    public void clean_StringWithSpecialCharacters_ReturnCleanedString() {
        String str = "Te#st123";
        assertEquals("TEST", soundexUtils.clean(str));
    }

    @Test
    public void clean_StringWithUpperCaseLetters_ReturnUpperCaseString() {
        String str = "Test";
        assertEquals("TEST", soundexUtils.clean(str));
    }

    @Test
    public void clean_StringWithLowerCaseLetters_ReturnUpperCaseString() {
        String str = "test";
        assertEquals("TEST", soundexUtils.clean(str));
    }

    // Test for the difference() method

    @Test
    public void difference_SameStrings_ReturnStringSize() throws EncoderException {
        StringEncoder encoder = new Soundex();
        String s1 = "Test";
        String s2 = "TEST";
        int expected = s1.length();
        int actual = soundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void difference_EncoderException_ThrowEncoderException() throws EncoderException {
        StringEncoder encoder = new Soundex() {
            @Override
            public String encode(String source) throws EncoderException {
                throw new EncoderException("Error encoding string");
            }
        };
        String s1 = "Test";
        String s2 = "TEST";
        soundexUtils.difference(encoder, s1, s2);
    }

    // Test for the differenceEncoded() method

    @Test
    public void differenceEncoded_SameEncodedStrings_ReturnStringSize() {
        String es1 = "T234";
        String es2 = "T234";
        int expected = es1.length();
        int actual = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void differenceEncoded_DifferentEncodedStrings_ReturnZero() {
        String es1 = "T234";
        String es2 = "S987";
        int expected = 0;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

    @Test
    public void differenceEncoded_NullStrings_ReturnZero() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int actual = soundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, actual);
    }

}