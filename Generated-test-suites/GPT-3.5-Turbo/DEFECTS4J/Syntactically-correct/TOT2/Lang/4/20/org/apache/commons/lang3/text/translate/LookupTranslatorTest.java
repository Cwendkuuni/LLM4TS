import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_EmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("abc", output.toString());
    }

    @Test
    public void testTranslate_SingleEntry() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder input = new StringBuilder("abc");
        StringBuilder output = new StringBuilder();
        assertEquals(3, translator.translate(input, 0, output));
        assertEquals("123", output.toString());
    }

    @Test
    public void testTranslate_MultipleEntries() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"}, new CharSequence[]{"def", "456"});
        StringBuilder input = new StringBuilder("abcdef");
        StringBuilder output = new StringBuilder();
        assertEquals(3, translator.translate(input, 3, output));
        assertEquals("456", output.toString());
    }

    @Test
    public void testTranslate_NoMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringBuilder input = new StringBuilder("def");
        StringBuilder output = new StringBuilder();
        assertEquals(0, translator.translate(input, 0, output));
        assertEquals("", output.toString());
    }

}