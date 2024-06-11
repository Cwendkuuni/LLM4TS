import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new NumericEntityEscaper(EntityArrays.BASIC_ESCAPE());
        String input = "<&\"";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("&lt;&amp;&quot;", writer.toString());
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String result = translator.translate(null);
        assertNotNull(result);
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String result = translator.translate("");
        assertNotNull(result);
    }

    @Test
    public void testTranslate_EdgeCases() throws IOException {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "\uFFFF";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("\\uFFFF", writer.toString());

        input = "\u0000";
        writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("\\u0000", writer.toString());
    }

    @Test
    public void testTranslate_IOExceptionThrown() {
        CharSequenceTranslator translator = new UnicodeEscaper() {
            @Override
            protected void translate(int codepoint, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        String input = "abc";
        try {
            translator.translate(input);
        } catch (RuntimeException e) {
            assertEquals("Test IOException", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() throws IOException {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "abc";
        translator.translate(input, null);
    }

    @Test
    public void testTranslate_IndexOutOfBounds() throws IOException {
        CharSequenceTranslator translator = new UnicodeEscaper();
        String input = "abc";
        StringWriter writer = new StringWriter();
        translator.translate(input, 10, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LocaleEnglish() {
        String hex = CharSequenceTranslator.hex(65);
        assertEquals("41", hex);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new UnicodeEscaper();
        CharSequenceTranslator translator2 = new NumericEntityEscaper(EntityArrays.BASIC_ESCAPE());
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        assertNotNull(combinedTranslator);
    }
}