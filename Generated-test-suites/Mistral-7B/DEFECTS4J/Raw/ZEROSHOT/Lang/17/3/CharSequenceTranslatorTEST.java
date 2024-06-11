package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NoOpCharSequenceTranslator;

import org.junit.Test;

/**
 * Tests for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new MyTranslator();
        Writer writer = new StringWriter();
        CharSequence input = CharSequenceUtils.asCharSequence("test");

        int translatedLength = translator.translate(input, 0, writer);
        String result = writer.toString();
        assertEquals("test translated", result);
        assertEquals(input.length(), translatedLength);

        // test empty input
        writer = new StringWriter();
        translatedLength = translator.translate(CharSequenceUtils.EMPTY, 0, writer);
        assertNull(writer.toString());
        assertEquals(0, translatedLength);

        // test null input
        try {
            translator.translate(null, 0, writer);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateToString() throws Exception {
        CharSequenceTranslator translator = new MyTranslator();
        String result = translator.translate("test");
        assertEquals("test translated", result);

        result = translator.translate(CharSequenceUtils.EMPTY);
        assertNull(result);

        try {
            translator.translate(null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     */
    @Test
    public void testTranslateToWriterWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new NoOpCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyTranslator();
        CharSequenceTranslator aggregatedTranslator = translator1.with(translator2);

        Writer writer = new StringWriter();
        CharSequence input = CharSequenceUtils.asCharSequence("test");

        int translatedLength = aggregatedTranslator.translate(input, 0, writer);
        String result = writer.toString();
        assertEquals("test translated1 translated2", result);
        assertEquals(input.length(), translatedLength);
    }

    /**
     * Test {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateToStringWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new NoOpCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyTranslator();
        CharSequenceTranslator aggregatedTranslator = translator1.with(translator2);

        String result = aggregatedTranslator.translate("test");
        assertEquals("test translated1 translated2", result);
    }

    /**
     * Test {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method with
     * surrogate pairs.
     */
    @Test
    public void testTranslateWithSurrogatePairs() throws Exception {
        CharSequenceTranslator translator = new MyTranslator();
        Writer writer = new StringWriter();
        CharSequence input = CharSequenceUtils.asCharSequence("\uD800\uDC00\uD800\uDC01");

        int translatedLength = translator.translate(input, 0, writer);
        String result = writer.toString();
        assertEquals("\uD834\uDD1E\uD834\uDD1F", result);
        assertEquals(input.length(), translatedLength);
    }

    private static class MyTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c)) {
                out.write(hex(Character.digit(c, 16)).toCharArray());
                return Character.charCount(c);
            }
            out.write(c);
            return 1;
        }
    }

}