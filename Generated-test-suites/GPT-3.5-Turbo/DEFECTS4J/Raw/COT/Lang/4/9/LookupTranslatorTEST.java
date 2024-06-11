
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    // Test constructor with single lookup table
    @Test
    public void testConstructor_SingleLookupTable() {
        CharSequence[][] lookupTable = {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable);

        assertNotNull(translator);
    }

    // Test constructor with multiple lookup tables
    @Test
    public void testConstructor_MultipleLookupTables() {
        CharSequence[][] lookupTable1 = {
                {"a", "A"},
                {"b", "B"}
        };
        CharSequence[][] lookupTable2 = {
                {"c", "C"},
                {"d", "D"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable1, lookupTable2);

        assertNotNull(translator);
    }

    // Test translate method with matching lookup
    @Test
    public void testTranslate_MatchingLookup() throws IOException {
        CharSequence[][] lookupTable = {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable);

        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        int result = translator.translate(input, 0, output);

        assertEquals(1, result);
        assertEquals("A", output.toString());
    }

    // Test translate method with non-matching lookup
    @Test
    public void testTranslate_NonMatchingLookup() throws IOException {
        CharSequence[][] lookupTable = {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable);

        StringBuilder input = new StringBuilder("def");
        StringBuilder output = new StringBuilder();
        int result = translator.translate(input, 0, output);

        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    // Test translate method with input shorter than shortest lookup
    @Test
    public void testTranslate_ShortInput() throws IOException {
        CharSequence[][] lookupTable = {
                {"ab", "AB"},
                {"cd", "CD"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable);

        StringBuilder input = new StringBuilder("a");
        StringBuilder output = new StringBuilder();
        int result = translator.translate(input, 0, output);

        assertEquals(0, result);
        assertEquals("", output.toString());
    }

    // Test translate method with input longer than longest lookup
    @Test
    public void testTranslate_LongInput() throws IOException {
        CharSequence[][] lookupTable = {
                {"ab", "AB"},
                {"cd", "CD"}
        };
        LookupTranslator translator = new LookupTranslator(lookupTable);

        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        int result = translator.translate(input, 0, output);

        assertEquals(2, result);
        assertEquals("AB", output.toString());
    }
}

