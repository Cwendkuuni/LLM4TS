
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_withMatchingLookup() {
        LookupTranslator translator = new LookupTranslator(new String[][]{{"a", "1"}, {"b", "2"}});
        StringBuffer output = new StringBuffer();
        int translated = translator.translate("abc", 0, output);
        assertEquals(1, translated);
        assertEquals("1", output.toString());
    }

    @Test
    public void testTranslate_withNoMatchingLookup() {
        LookupTranslator translator = new LookupTranslator(new String[][]{{"a", "1"}, {"b", "2"}});
        StringBuffer output = new StringBuffer();
        int translated = translator.translate("xyz", 0, output);
        assertEquals(0, translated);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withPartialMatch() {
        LookupTranslator translator = new LookupTranslator(new String[][]{{"abc", "1"}, {"ab", "2"}});
        StringBuffer output = new StringBuffer();
        int translated = translator.translate("abcd", 0, output);
        assertEquals(2, translated);
        assertEquals("2cd", output.toString());
    }

    @Test
    public void testTranslate_withEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new String[][]{{"a", "1"}, {"b", "2"}});
        StringBuffer output = new StringBuffer();
        int translated = translator.translate("", 0, output);
        assertEquals(0, translated);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withEmptyLookup() {
        LookupTranslator translator = new LookupTranslator();
        StringBuffer output = new StringBuffer();
        int translated = translator.translate("abc", 0, output);
        assertEquals(0, translated);
        assertEquals("", output.toString());
    }
}
