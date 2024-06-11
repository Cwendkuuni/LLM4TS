import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }

    @Test
    public void testMetaphone_emptyString_returnEmptyString() {
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }

    @Test
    public void testMetaphone_singleCharacterString_returnUpperCaseString() {
        String result = metaphone.metaphone("a");
        assertEquals("A", result);
    }

    @Test
    public void testMetaphone_twoCharacterString_returnUpperCaseString() {
        String result = metaphone.metaphone("ab");
        assertEquals("AB", result);
    }

    @Test
    public void testMetaphone_threeCharacterString_returnUpperCaseString() {
        String result = metaphone.metaphone("abc");
        assertEquals("ABK", result);
    }

    @Test
    public void testMetaphone_fourCharacterString_returnUpperCaseString() {
        String result = metaphone.metaphone("abcd");
        assertEquals("ABKT", result);
    }

    @Test
    public void testMetaphone_hangOnMetaphone_returnExpectedMetaphone() {
        String result = metaphone.metaphone("hanging on metaphone");
        assertEquals("HNKNONMETFN", result);
    }

    @Test
    public void testMetaphone_specialCharacters_returnMetaphoneIgnoringSpecialCharacters() {
        String result = metaphone.metaphone("123!@#abc");
        assertEquals("ABK", result);
    }

    @Test
    public void testEncode_inputNotString_throwEncoderException() {
        try {
            metaphone.encode(123);
            fail("Should have thrown EncoderException");
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Metaphone encode is not of type java.lang.String", e.getMessage());
        }
    }

    @Test
    public void testEncode_validString_returnMetaphone() {
        String result = metaphone.encode("abc");
        assertEquals("ABK", result);
    }

    @Test
    public void testIsMetaphoneEqual_identicalStrings_returnTrue() {
        boolean result = metaphone.isMetaphoneEqual("abc", "ABC");
        assertTrue(result);
    }

    @Test
    public void testIsMetaphoneEqual_differentStrings_returnFalse() {
        boolean result = metaphone.isMetaphoneEqual("abc", "def");
        assertFalse(result);
    }

    @Test
    public void testIsMetaphoneEqual_caseSensitiveStrings_returnFalse() {
        boolean result = metaphone.isMetaphoneEqual("abc", "aBc");
        assertFalse(result);
    }
}