import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"k", "v"});

        StringWriter writer = new StringWriter();
        translator.translate("key", 0, writer);

        assertEquals("Value", writer.toString());
    }

    @Test
    public void testTranslate_notFound() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"k", "v"});

        StringWriter writer = new StringWriter();
        int result = translator.translate("noMatch", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_partialMatch() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"key", "val"});

        StringWriter writer = new StringWriter();
        int result = translator.translate("keychain", 0, writer);

        assertEquals(3, result);
        assertEquals("valchain", writer.toString());
    }

    @Test
    public void testTranslate_emptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"key", "val"});

        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_emptyLookupTable() throws IOException {
        LookupTranslator translator = new LookupTranslator();

        StringWriter writer = new StringWriter();
        int result = translator.translate("key", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}