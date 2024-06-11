import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new ConcreteTranslator();
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        String result = translator.translate("");
        assertEquals("", result);
    }

    @Test
    public void testTranslate_withValidInput_shouldTranslateCorrectly() {
        String input = "Hello, world!";
        String expected = "Translated Hello, world!";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws Exception {
        translator.translate("input", null);
    }

    @Test
    public void testHex_withValidCodepoint_shouldReturnCorrectHexString() {
        int codepoint = 65;
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    private class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) {
            if (input == null || index < 0 || index >= input.length() || out == null) {
                throw new IllegalArgumentException();
            }
            try {
                out.write("Translated " + input.subSequence(index, input.length()));
                return input.length() - index;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}