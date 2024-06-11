import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();

        // Typical use case
        String input = "hello";
        String output = translator.translate(input);
        assertEquals("Translation successful", "1:hello", output);

        // Edge case - empty input
        input = "";
        output = translator.translate(input);
        assertEquals("Empty input", "", output);

        // Error scenario - null input
        assertNull("Null input", translator.translate(null));
    }

    @Test
    public void testTranslateWithIndex() throws IOException {
        CharSequenceTranslator translator = new CustomTranslator();

        // Typical use case
        StringWriter writer = new StringWriter();
        int index = 0;
        int count = translator.translate("hello", index, writer);
        assertEquals("Expected codepoints consumed", 1, count);
        assertEquals("Output after translation", "1:hello", writer.toString());

        // Edge case - index beyond input length
        writer = new StringWriter();
        index = 10;
        count = translator.translate("hello", index, writer);
        assertEquals("Expected codepoints consumed", 0, count);
        assertEquals("Output after translation", "", writer.toString());
    }

    @Test
    public void testHex() {
        // Typical use case
        String hex = CharSequenceTranslator.hex(65);
        assertEquals("Hexadecimal representation of codepoint", "41", hex);

        // Edge case - lower case input
        hex = CharSequenceTranslator.hex(97);
        assertEquals("Hexadecimal representation of lower case codepoint", "61", hex);
    }

    private class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'h') {
                out.write("1:");
                return 1;
            }
            return 0;
        }
    }
}