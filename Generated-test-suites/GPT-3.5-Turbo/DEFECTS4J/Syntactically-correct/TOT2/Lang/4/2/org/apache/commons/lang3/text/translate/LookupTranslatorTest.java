import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder sb = new StringBuilder();
        try {
            translator.translate("foo", 0, sb);
            assertEquals("bar", sb.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTranslate_invalidIndex() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder sb = new StringBuilder();
        try {
            translator.translate("foo", 5, sb);
            assertEquals("", sb.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder sb = new StringBuilder();
        try {
            translator.translate("", 0, sb);
            assertEquals("", sb.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testTranslate_notFound() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder sb = new StringBuilder();
        try {
            translator.translate("xyz", 0, sb);
            assertEquals("", sb.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testInvalidLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        StringBuilder sb = new StringBuilder();
        try {
            translator.translate("foo", 0, sb);
            assertEquals("", sb.toString());
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }
}