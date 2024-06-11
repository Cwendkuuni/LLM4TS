import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_Basic() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"hello", "world"});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("hello", 0, output);
        } catch (Exception e) {
            fail("Should not throw exception for valid input");
        }
        assertEquals("world", output.toString());
    }

    @Test
    public void testTranslate_EmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"hello", "world"});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("", 0, output);
        } catch (Exception e) {
            fail("Should not throw exception for empty input");
        }
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_NoMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"hello", "world"});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("foo", 0, output);
        } catch (Exception e) {
            fail("Should not throw exception for no match");
        }
        assertEquals(0, output.length());
    }

    @Test
    public void testTranslate_LongestMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "AA", "ab", "AB"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("abcde", 0, output);
            assertEquals(2, result);
            assertEquals("AB", output.toString());
        } catch (Exception e) {
            fail("Should not throw exception for longest match");
        }
    }

    @Test
    public void testTranslate_ShortestMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"aa", "AA", "a", "A"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("aab", 0, output);
            assertEquals(1, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Should not throw exception for shortest match");
        }
    }

    @Test
    public void testTranslate_UnicodeSupport() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"こんにちは", "Hello"});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("こんにちは", 0, output);
        } catch (Exception e) {
            fail("Should not throw exception for Unicode input");
        }
        assertEquals("Hello", output.toString());
    }

}