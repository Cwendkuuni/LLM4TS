import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();

        int result = translator.translate("foo", 0, writer);

        assertEquals(3, result);
        assertEquals("bar", writer.toString());
    }

    @Test
    public void testTranslate_edgeCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"}, new CharSequence[]{"baz", "qux"});
        StringWriter writer = new StringWriter();

        int result = translator.translate("bazfoo", 3, writer);

        assertEquals(3, result);
        assertEquals("qux", writer.toString());
    }

    @Test
    public void testTranslate_noMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();

        int result = translator.translate("baz", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();

        int result = translator.translate("", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}