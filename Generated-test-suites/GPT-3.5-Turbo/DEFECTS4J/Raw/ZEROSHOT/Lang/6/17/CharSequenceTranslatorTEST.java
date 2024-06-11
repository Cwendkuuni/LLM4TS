import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = "Hello World";
        String expectedOutput = "Hello World";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslate_nullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = null;
        assertNull(translator.translate(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_nullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };

        String input = "Hello World";
        translator.translate(input, null);
    }

    @Test
    public void testTranslate_customTranslator() {
        CharSequenceTranslator customTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'o') {
                    out.write('0');
                    return 1;
                } else {
                    out.write(input.charAt(index));
                    return 1;
                }
            }
        };

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toLowerCase(input.charAt(index)));
                return 1;
            }
        };

        CharSequenceTranslator resultTranslator = translator.with(customTranslator);

        String input = "Hello World";
        String expectedOutput = "hell0 w0rld";
        String actualOutput = resultTranslator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHex_lowercase() {
        int codepoint = 97;
        String expectedOutput = "61";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, actualOutput);
    }
}