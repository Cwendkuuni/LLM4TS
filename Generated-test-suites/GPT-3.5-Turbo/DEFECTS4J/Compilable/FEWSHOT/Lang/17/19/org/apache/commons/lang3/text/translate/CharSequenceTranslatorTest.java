package org.apache.commons.lang3.text.translate;

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
                // implementation of translation here
                return 0;
            }
        };

        // test with null input
        assertNull(translator.translate(null));

        // test with empty input
        assertEquals("", translator.translate(""));

        // test with non-empty input
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // implementation of translation here
                return 0;
            }
        };

        // test with null input
        StringWriter writer1 = new StringWriter();
        translator.translate(null, writer1);
        assertEquals("", writer1.toString());

        // test with empty input
        StringWriter writer2 = new StringWriter();
        translator.translate("", writer2);
        assertEquals("", writer2.toString());

        // test with non-empty input
        StringWriter writer3 = new StringWriter();
        translator.translate("abc", writer3);
        assertEquals("abc", writer3.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // implementation of translation here
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // implementation of translation here
                return 0;
            }
        };

        CharSequenceTranslator translator3 = translator1.with(translator2);

        // test that the returned translator is an instance of AggregateTranslator
        assertEquals(AggregateTranslator.class, translator3.getClass());
    }

    @Test
    public void testHex() {
        // test with lowercase hexadecimal string
        assertEquals("61", CharSequenceTranslator.hex(97));

        // test with uppercase hexadecimal string
        assertEquals("41", CharSequenceTranslator.hex(65));
    }
}