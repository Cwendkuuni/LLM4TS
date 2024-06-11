import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_BasicCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("abcdef", 0, output);
            assertEquals(3, result);
            assertEquals("123def", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_ShortInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("ab", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_LongInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("abcdefghijklmnop", 0, output);
            assertEquals(3, result);
            assertEquals("123defghijklmnop", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_NoMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate("xyz", 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslate_Exception() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder output = new StringBuilder();
        try {
            int result = translator.translate(null, 0, output);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}