import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new ConcreteTranslator();
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_withValidInput_shouldTranslateCorrectly() {
        String input = "Hello, <script>alert('XSS');</script> World!";
        String expectedOutput = "Hello, <script>alert(&apos;XSS&apos;);</script> World!";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslate_withInvalidInput_shouldHandleError() {
        String input = "Hello, <script>alert('XSS');</script World!";
        try {
            translator.translate(input);
        } catch (RuntimeException e) {
            assertEquals(IOException.class, e.getCause().getClass());
            assertEquals("Unterminated &lt;script tag encountered", e.getMessage());
        }
    }

    @Test
    public void testTranslate_withSurrogatePair_shouldTranslateCorrectly() {
        String input = "\uD83D\uDC68\uD83D\uDC69";
        String expectedOutput = "<span>&#128104;&#128105;</span>";
        assertEquals(expectedOutput, translator.translate(input));
    }

    @Test
    public void testTranslate_withWriter_shouldTranslateAndWriteToWriterCorrectly() throws IOException {
        String input = "Hello, <script>alert('XSS');</script> World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expectedOutput = "Hello, <script>alert(&apos;XSS&apos;);</script> World!";
        assertEquals(expectedOutput, writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws IOException {
        String input = "Hello, <script>alert('XSS');</script> World!";
        translator.translate(input, null);
    }

    @Test
    public void testTranslate_withInvalidInputAndWriter_shouldHandleError() {
        String input = "Hello, <script>alert('XSS');</script World!";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            assertEquals("Unterminated &lt;script tag encountered", e.getMessage());
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testWith_withMultipleTranslators_shouldMergeCorrectly() {
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new ConcreteTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String input = "Hello, <script>alert('XSS');</script> World!";
        String expectedOutput = "Hello, <script>alert(&apos;XSS&apos;);</script> World!";
        assertEquals(expectedOutput, combinedTranslator.translate(input));
    }

    @Test
    public void testHex_withValidCodepoint_shouldReturnCorrectHexadecimalString() {
        int codepoint = 128104;
        String expectedHex = "1F468";
        assertEquals(expectedHex, CharSequenceTranslator.hex(codepoint));
    }

    private static class ConcreteTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException("Invalid input or writer");
            }
            out.write(input.toString().replace("'", "&apos;").replace("<", "&lt;").replace(">", "&gt;"));
            return input.length();
        }
    }
}