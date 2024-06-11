import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_LookupExists_TranslationSuccess() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("a b", 0, writer);
        assertEquals(1, result);
        assertEquals("1", writer.toString());
    }

    @Test
    public void testTranslate_LookupNotExists_TranslationNotDone() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("c", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LookupPartialMatch_TranslationNotDone() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("ab", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LookupMultipleMatch_TranslationSuccess() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"bb", "22"}});
        StringWriter writer = new StringWriter();
        int result = translator.translate("a bb", 0, writer);
        assertEquals(2, result);
        assertEquals("1 22", writer.toString());
    }
}