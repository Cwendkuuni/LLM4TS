
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_CharSequenceIntWriter() throws IOException {
        // Test with null input
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        assertEquals(0, translator.translate(null, 0, writer));
        assertEquals("", writer.toString());

        // Test with empty input
        assertEquals(0, translator.translate("", 0, writer));
        assertEquals("", writer.toString());

        // Test with regular input
        assertEquals(3, translator.translate("abc", 0, writer));
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_CharSequence() {
        // Test with null input
        CharSequenceTranslator translator = new TestTranslator();
        assertEquals(null, translator.translate(null));

        // Test with empty input
        assertEquals("", translator.translate(""));

        // Test with regular input
        assertEquals("abcde", translator.translate("abcde"));
    }

    @Test
    public void testTranslate_CharSequenceWriter() throws IOException {
        // Test with null input
        CharSequenceTranslator translator = new TestTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());

        // Test with empty input
        translator.translate("", writer);
        assertEquals("", writer.toString());

        // Test with regular input
        translator.translate("abcde", writer);
        assertEquals("abcde", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_CharSequenceWriter_NullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        translator.translate("abcde", null);
    }

    @Test
    public void testWith_CharSequenceTranslatorArray() {
        CharSequenceTranslator[] translators = new CharSequenceTranslator[2];
        translators[0] = new TestTranslator();
        translators[1] = new TestTranslator();

        CharSequenceTranslator translator = new TestTranslator().with(translators);
        assertEquals(translators.length + 1, ((TestTranslator) translator).getTranslators().length);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("7A", CharSequenceTranslator.hex(122));
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null) {
                return 0;
            }
            out.write(input.toString().substring(index));
            return input.length() - index;
        }

        public CharSequenceTranslator[] getTranslators() {
            return new CharSequenceTranslator[0];
        }
    }
}

