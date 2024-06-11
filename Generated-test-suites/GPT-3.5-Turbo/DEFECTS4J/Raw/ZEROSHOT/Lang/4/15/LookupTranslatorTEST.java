import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructorWithNullLookup() {
        CharSequence[][] lookup = null;
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator.getLookupMap());
        assertEquals(0, translator.getShortest());
        assertEquals(0, translator.getLongest());
    }

    @Test
    public void testConstructorWithEmptyLookup() {
        CharSequence[][] lookup = new CharSequence[0][2];
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator.getLookupMap());
        assertEquals(0, translator.getShortest());
        assertEquals(0, translator.getLongest());
    }

    @Test
    public void testConstructorWithNonEmptyLookup() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator.getLookupMap());
        assertEquals(1, translator.getShortest());
        assertEquals(1, translator.getLongest());
    }

    @Test
    public void testTranslateWithEmptyInput() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder input = new StringBuilder();
        StringBuilder output = new StringBuilder();
        try {
            translator.translate(input, 0, output);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        assertEquals(0, output.length());
    }

    @Test
    public void testTranslateWithValidInput() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        try {
            translator.translate(input, 0, output);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        assertEquals("123", output.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"}
        };
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder input = new StringBuilder("def");
        StringBuilder output = new StringBuilder();
        try {
            translator.translate(input, 0, output);
        } catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
        assertEquals(0, output.length());
    }
}