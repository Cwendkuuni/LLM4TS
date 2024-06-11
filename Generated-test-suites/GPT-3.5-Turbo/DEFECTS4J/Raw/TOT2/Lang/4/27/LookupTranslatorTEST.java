import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate_basicCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringBuilder output = new StringBuilder();
        translator.translate("abc", 0, output);
        assertEquals("12c", output.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringBuilder output = new StringBuilder();
        translator.translate("", 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_edgeCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "123"}});
        StringBuilder output = new StringBuilder();
        translator.translate("abcd", 0, output);
        assertEquals("123d", output.toString());
    }

    @Test
    public void testTranslate_notFound() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        StringBuilder output = new StringBuilder();
        translator.translate("def", 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_partialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "123"}});
        StringBuilder output = new StringBuilder();
        translator.translate("abcdef", 0, output);
        assertEquals("123def", output.toString());
    }
}