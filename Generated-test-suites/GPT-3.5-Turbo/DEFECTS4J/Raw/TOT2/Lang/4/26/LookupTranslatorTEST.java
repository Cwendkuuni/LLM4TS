import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("foo", 0, writer);
            assertEquals(3, result);
            assertEquals("bar", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_invalidInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("baz", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_longest() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar", "hello", "world"});
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("hello", 0, writer);
            assertEquals(5, result);
            assertEquals("world", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_edgeCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar", "baz", "qux"});
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("bazbaz", 0, writer);
            assertEquals(3, result);
            assertEquals("quxqux", writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}