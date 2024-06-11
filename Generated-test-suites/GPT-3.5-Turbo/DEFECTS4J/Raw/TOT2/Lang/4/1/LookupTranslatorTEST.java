import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[] { "a", "1" });
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("abc", 0, output);
            assertEquals("1bc", output.toString());
        } catch (Exception e) {
            fail("Exception thrown for basic case");
        }
    }

    @Test
    public void testTranslate_edgeCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[] { "ab", "12" });
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("abcdef", 0, output);
            assertEquals("12cdef", output.toString());
        } catch (Exception e) {
            fail("Exception thrown for edge case");
        }
    }

    @Test
    public void testTranslate_multipleMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[] { "a", "1" }, new CharSequence[] { "b", "2" });
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("abc", 0, output);
            assertEquals("12c", output.toString());
        } catch (Exception e) {
            fail("Exception thrown for multiple matches case");
        }
    }

    @Test
    public void testTranslate_noMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[] { "x", "1" });
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("abc", 0, output);
            assertEquals("abc", output.toString());
        } catch (Exception e) {
            fail("Exception thrown for no match case");
        }
    }

    @Test
    public void testTranslate_exceptionHandling() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[] { "a", "1" });
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("abc", 4, output); // Index out of bounds
            fail("Expected IOException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }
}