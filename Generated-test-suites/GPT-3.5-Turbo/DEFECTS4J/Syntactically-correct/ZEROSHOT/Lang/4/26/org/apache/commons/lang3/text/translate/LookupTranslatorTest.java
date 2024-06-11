import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator();
        
        // Test empty input
        assertEquals(0, translator.translate("", 0, null));
        
        // Test lookup with no entries
        assertEquals(0, translator.translate("test", 0, null));
        
        // Test lookup with one entry
        translator = new LookupTranslator(
            new CharSequence[]{ "a", "A" }
        );
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals("A", out.toString());
        
        // Test lookup with multiple entries
        translator = new LookupTranslator(
            new CharSequence[]{ "a", "A" },
            new CharSequence[]{ "b", "B" },
            new CharSequence[]{ "c", "C" }
        );
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals("A", out.toString());
        assertEquals(1, translator.translate("b", 0, null));
        assertEquals("B", out.toString());
        assertEquals(1, translator.translate("c", 0, null));
        assertEquals("C", out.toString());
        
        // Test lookup with multiple entries and longer input
        translator = new LookupTranslator(
            new CharSequence[]{ "aa", "AA" },
            new CharSequence[]{ "bb", "BB" },
            new CharSequence[]{ "cc", "CC" }
        );
        assertEquals(2, translator.translate("aa", 0, null));
        assertEquals("AA", out.toString());
        assertEquals(2, translator.translate("bb", 0, null));
        assertEquals("BB", out.toString());
        assertEquals(2, translator.translate("cc", 0, null));
        assertEquals("CC", out.toString());
        
        // Test lookup with multiple entries and longer input, should prefer longest match
        translator = new LookupTranslator(
            new CharSequence[]{ "aaa", "AAA" },
            new CharSequence[]{ "aa", "AA" },
            new CharSequence[]{ "a", "A" }
        );
        assertEquals(3, translator.translate("aaa", 0, null));
        assertEquals("AAA", out.toString());
        assertEquals(2, translator.translate("aa", 0, null));
        assertEquals("AA", out.toString());
        assertEquals(1, translator.translate("a", 0, null));
        assertEquals("A", out.toString());
        
        // Test lookup with multiple entries and longer input, should prefer longest match and ignore case
        translator = new LookupTranslator(
            new CharSequence[]{ "AAA", "aaa" },
            new CharSequence[]{ "AA", "aa" },
            new CharSequence[]{ "A", "a" }
        );
        assertEquals(3, translator.translate("AAA", 0, null));
        assertEquals("aaa", out.toString());
        assertEquals(2, translator.translate("AA", 0, null));
        assertEquals("aa", out.toString());
        assertEquals(1, translator.translate("A", 0, null));
        assertEquals("a", out.toString());
    }
}