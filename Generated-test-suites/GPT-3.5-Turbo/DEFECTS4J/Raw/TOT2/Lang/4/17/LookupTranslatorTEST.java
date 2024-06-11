import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_Success() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "A"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("apple", 0, output);
            assertEquals(1, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testTranslate_NoMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "A"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("test", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testTranslate_LookupNull() {
        LookupTranslator translator = new LookupTranslator(null);
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("abc", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception occurred");
        }
    }

    @Test
    public void testTranslate_Exception() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "A"});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("apple", 10, output);
            fail("Expected IOException was not thrown");
        } catch (IOException e) {
            // Exception expected
        }
    }

}