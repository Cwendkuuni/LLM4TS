import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder result = new StringBuilder();
        translator.translate("abc", 0, result);
        assertEquals("123", result.toString());
    }

    @Test
    public void testTranslate_edgeCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"});
        StringBuilder result = new StringBuilder();
        translator.translate("abc", 0, result);
        assertEquals("1", result.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder result = new StringBuilder();
        translator.translate("", 0, result);
        assertEquals("", result.toString());
    }

    @Test
    public void testTranslate_longestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"longestKey", "123"});
        StringBuilder result = new StringBuilder();
        translator.translate("longestKey", 0, result);
        assertEquals("123", result.toString());
    }

    @Test
    public void testTranslate_nullOutput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", null});
        StringBuilder result = new StringBuilder();
        translator.translate("abc", 0, result);
        assertEquals("", result.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_exception() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        translator.translate("xyz", 0, new StringBuilder());
    }
}