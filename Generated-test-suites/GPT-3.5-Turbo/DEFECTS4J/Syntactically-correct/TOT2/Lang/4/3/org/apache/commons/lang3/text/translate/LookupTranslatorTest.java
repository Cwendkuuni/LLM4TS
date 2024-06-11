import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        translator.translate("foo", 0, writer);
        assertEquals("bar", writer.toString());
    }

    @Test
    public void testTranslate_edgeCase() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"}, new CharSequence[]{"bar", "baz"});
        StringWriter writer = new StringWriter();
        translator.translate("bar", 0, writer);
        assertEquals("baz", writer.toString());
    }

    @Test
    public void testTranslate_noMatch() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("baz", 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_emptyInput() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_exceptionHandling() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
            // If an exception is not thrown, test fails
            assert false;
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }
}