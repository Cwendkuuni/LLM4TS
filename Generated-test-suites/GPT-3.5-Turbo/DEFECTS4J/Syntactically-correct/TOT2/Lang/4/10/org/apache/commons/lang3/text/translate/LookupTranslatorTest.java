import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"cat", "dog"}, {"apple", "banana"}});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("apple", 0, output);
            assertEquals("banana", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testTranslate_edgeCaseInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"123", "456"}});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("123", 0, output);
            assertEquals("456", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testTranslate_invalidInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"cat", "dog"}});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("apple", 0, output);
            assertEquals("apple", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown for invalid lookup");
        }
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate("", 0, output);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown for empty input");
        }
    }

    @Test
    public void testTranslate_nullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"cat", "dog"}});
        StringBuilder output = new StringBuilder();
        try {
            translator.translate(null, 0, output);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception should not be thrown for null input");
        }
    }

}