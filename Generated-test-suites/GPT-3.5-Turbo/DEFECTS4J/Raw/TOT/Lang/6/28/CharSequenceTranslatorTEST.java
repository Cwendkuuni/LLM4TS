import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private static class BogusCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("BOGUS");
            return 1;
        }
    }

    private static class EmptyCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    }

    private static class TranslatorWhichThrowsException extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new AggregateTranslator(
                new LookupTranslator(createEntityMap()),
                new UnicodeEscaper(),
                new JavaUnicodeEscaper(),
                new NumericEntityUnescaper(),
                new UnicodeUnescaper(),
                new BogusCharSequenceTranslator()
        );
    }

    @Test
    public void testTranslate_withValidInput_success() throws IOException {
        String input = "&amp; &apos; &quot; &lt; &gt; &lt &gt &; &auml; &ouml; &uuml; &nbsp;";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("Translations are incorrect", consumed, input.length());
        assertEquals("Translated output is incorrect", "& ' \" < > < > ; ä ö ü ", writer.toString());
    }

    @Test
    public void testTranslate_withNullInput_success() throws IOException {
        String input = null;
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("Consumed should be 0 for null input", 0, consumed);
        assertEquals("Translated output is incorrect", "", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_throwException() throws IOException {
        String input = "&amp;";
        Writer writer = null;

        translator.translate(input, 0, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslate_withException_throwException() throws IOException {
        CharSequenceTranslator translator = new TranslatorWhichThrowsException();
        String input = "&amp;";
        StringWriter writer = new StringWriter();

        translator.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_withEmptyTranslator_success() throws IOException {
        CharSequenceTranslator translator = new EmptyCharSequenceTranslator();
        String input = "&amp;";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("Consumed should be 0 for empty translator", 0, consumed);
        assertEquals("Translated output is incorrect", input, writer.toString());
    }

    @Test
    public void testTranslate_withBogusTranslator_success() throws IOException {
        CharSequenceTranslator translator = new BogusCharSequenceTranslator();
        String input = "&amp; &apos; &quot; &lt; &gt; &lt &gt &; &auml; &ouml; &uuml; &nbsp;";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("Translations are incorrect", consumed, input.length());
        assertEquals("Translated output is incorrect", "BOGUS", writer.toString());
    }

    @Test
    public void testTranslate_withMultipleTranslators_success() throws IOException {
        CharSequenceTranslator translator = new AggregateTranslator(
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
                new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()),
                new NumericEntityUnescaper(),
                new UnicodeUnescaper(),
                new BogusCharSequenceTranslator()
        );
        String input = "&amp; &apos; &quot; &lt; &gt; &lt &gt &; &auml; &ouml; &uuml; &nbsp;";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);

        assertEquals("Translations are incorrect", consumed, input.length());
        assertEquals("Translated output is incorrect", "& ' \" < > < > ; ä ö ü ", writer.toString());
    }

    @Test
    public void testConstructor_withTranslators_success() {
        CharSequenceTranslator[] translators = {
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()),
                new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())
        };

        CharSequenceTranslator newTranslator = translator.with(translators);

        assertNotNull("New translator is null", newTranslator);
        assertEquals("Expected number of translators is incorrect", 3, ((AggregateTranslator) newTranslator).getTranslators().length);
    }

    @Test
    public void testHex_withValidCodepoint_success() {
        int codepoint = 65;

        String hex = CharSequenceTranslator.hex(codepoint);

        assertEquals("Hex value is incorrect", "41", hex);
    }

    private Map<CharSequence, CharSequence> createEntityMap() {
        Map<CharSequence, CharSequence> entityMap = new HashMap<>();
        entityMap.put("lt", "<");
        entityMap.put("gt", ">");
        entityMap.put("amp", "&");
        entityMap.put("nbsp", " ");
        entityMap.put("apos", "'");
        entityMap.put("quot", "\"");
        entityMap.put("auml", "ä");
        entityMap.put("ouml", "ö");
        entityMap.put("uuml", "ü");
        return entityMap;
    }
}