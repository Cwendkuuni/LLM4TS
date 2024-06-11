import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructorWithNullLookup() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNotNull(translator);
    }

    @Test
    public void testConstructorWithEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        assertNotNull(translator);
    }

    @Test
    public void testConstructorWithLookup() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder output = new StringBuilder();
        int result = translator.translate("", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateWithNoMatches() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder output = new StringBuilder();
        int result = translator.translate("def", 0, output);
        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateWithMatchingSubstring() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder output = new StringBuilder();
        int result = translator.translate("abc", 0, output);
        assertEquals(3, result);
        assertEquals("123", output.toString());
    }

    @Test
    public void testTranslateWithLongestSubstringMatch() {
        CharSequence[][] lookup = {
            {"ab", "1"},
            {"bc", "2"},
            {"cd", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder output = new StringBuilder();
        int result = translator.translate("abcde", 0, output);
        assertEquals(2, result);
        assertEquals("12cde", output.toString());
    }

    @Test
    public void testTranslateWithMultipleMatches() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"ab", "2"},
            {"abc", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder output = new StringBuilder();
        int result = translator.translate("abcd", 0, output);
        assertEquals(3, result);
        assertEquals("3d", output.toString());
    }

}