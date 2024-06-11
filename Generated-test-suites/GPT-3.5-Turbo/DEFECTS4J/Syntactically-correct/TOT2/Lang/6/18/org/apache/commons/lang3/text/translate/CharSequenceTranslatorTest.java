import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator(); // ConcreteCharSequenceTranslator is a concrete implementation of CharSequenceTranslator

        String input = "hello";
        String expected = "hello";

        String result = translator.translate(input);

        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator(); // ConcreteCharSequenceTranslator is a concrete implementation of CharSequenceTranslator

        String input = null;
        
        assertNull(translator.translate(input));        
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator(); // ConcreteCharSequenceTranslator is a concrete implementation of CharSequenceTranslator

        String input = "";
        String expected = "";

        String result = translator.translate(input);

        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithSpecialCharacters() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator(); // ConcreteCharSequenceTranslator is a concrete implementation of CharSequenceTranslator

        String input = "a&b<c>d";
        String expected = "a&amp;b&lt;c&gt;d";

        String result = translator.translate(input);

        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithException() {
        CharSequenceTranslator translator = new ConcreteCharSequenceTranslator(); // ConcreteCharSequenceTranslator is a concrete implementation of CharSequenceTranslator

        String input = "hello";
        Writer out = null;

        assertThrows(IOException.class, () -> {
            translator.translate(input, 0, out);
        });
    }

    @Test
    public void testHex() {
        int codepoint = 65; // 'A'
        String expected = "41";

        String result = CharSequenceTranslator.hex(codepoint);

        assertEquals(expected, result);
    }
}