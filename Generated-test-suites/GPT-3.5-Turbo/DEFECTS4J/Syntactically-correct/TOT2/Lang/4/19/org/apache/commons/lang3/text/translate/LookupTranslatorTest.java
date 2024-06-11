import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new String[]{"cat", "dog"});

        StringBuilder out = new StringBuilder();
        String input = "I have a cat";
        int result = translator.translate(input, 7, out);

        assertEquals(3, result);
        assertEquals("dog", out.toString());
    }

    @Test
    public void testTranslateWithNoMatch() {
        LookupTranslator translator = new LookupTranslator(new String[]{"apple", "orange"});

        StringBuilder out = new StringBuilder();
        String input = "I like apples";
        int result = translator.translate(input, 7, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new String[]{"apple", "orange"});

        StringBuilder out = new StringBuilder();
        String input = "";
        int result = translator.translate(input, 0, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMultipleMatches() {
        LookupTranslator translator = new LookupTranslator(new String[]{"apple", "orange"}, new String[]{"app", "fruit"});

        StringBuilder out = new StringBuilder();
        String input = "I like apple and apples";
        int result = translator.translate(input, 7, out);

        assertEquals(5, result);
        assertEquals("orange", out.toString());
    }

    @Test
    public void testTranslateWithLongInput() {
        LookupTranslator translator = new LookupTranslator(new String[]{"hello", "hi"});

        StringBuilder out = new StringBuilder();
        String input = "This is a very long input string that should not match";
        int result = translator.translate(input, 10, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}