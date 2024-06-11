import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        CharSequence input = "This is a test";
        String expectedOutput = "This is a test";

        StringBuilder output = new StringBuilder();
        int translatedLength = translator.translate(input, 0, output);

        assertEquals("Unexpected output from translate method", expectedOutput, output.toString());
        assertEquals("translate method should not return any translated length", 0, translatedLength);
    }

    @Test
    public void testLookupTranslatorWithOneEntryLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}});
        CharSequence input = "hello world";
        String expectedOutput = "hi world";

        StringBuilder output = new StringBuilder();
        int translatedLength = translator.translate(input, 0, output);

        assertEquals("Unexpected output from translate method", expectedOutput, output.toString());
        assertEquals("translate method should return translated length", 5, translatedLength);
    }

    @Test
    public void testLookupTranslatorWithMultipleEntriesLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "earth"}});
        CharSequence input = "hello world";
        String expectedOutput = "hi earth";

        StringBuilder output = new StringBuilder();
        int translatedLength = translator.translate(input, 0, output);

        assertEquals("Unexpected output from translate method", expectedOutput, output.toString());
        assertEquals("translate method should return translated length", 5, translatedLength);
    }

    @Test
    public void testLookupTranslatorWithPartialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "earth"}});
        CharSequence input = "hello there world";
        String expectedOutput = "hi there earth";

        StringBuilder output = new StringBuilder();
        int translatedLength = translator.translate(input, 0, output);

        assertEquals("Unexpected output from translate method", expectedOutput, output.toString());
        assertEquals("translate method should return translated length", 11, translatedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyLookupTableEntry() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{}, {"world", "earth"}});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{null, "world"}, {"world", "earth"}});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", null}});
    }
}