package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        CharSequence input = "TEST";
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assertEquals(1, consumed);
        assertEquals("T", writer.toString());

        index += consumed;
        consumed = translator.translate(input, index, writer);
        assertEquals(1, consumed);
        assertEquals("E", writer.toString());

        index += consumed;
        consumed = translator.translate(input, index, writer);
        assertEquals(1, consumed);
        assertEquals("S", writer.toString());

        index += consumed;
        consumed = translator.translate(input, index, writer);
        assertEquals(1, consumed);
        assertEquals("T", writer.toString());

        assertEquals(input.length() * 2, writer.toString().length());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateString() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        assertEquals("TEST", translator.translate("TEST"));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with null input and Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = null;
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequence input = "TEST";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator[])}.
     */
    @Test
    public void testWithTranslators() throws Exception {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator = translator1.with(translator2);

        StringWriter writer = new StringWriter();
        CharSequence input = "TEST";

        int consumed1 = translator1.translate(input, 0, writer);
        assertEquals(1, consumed1);
        assertEquals("T", writer.toString());

        int consumed2 = translator2.translate(input, 1, writer);
        assertEquals(1, consumed2);
        assertEquals("E", writer.toString());

        int consumed3 = translator.translate(input, 1, writer);
        assertEquals(1, consumed3);
        assertEquals("E", writer.toString());

        assertEquals(input.length() * 2, writer.toString().length());
    }

    /**
     * Test custom CharSequenceTranslator implementation.
     */
    private static class MyCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.isSurrogate(Character.codePointAt(input, index)) ? Character.highSurrogate(input, index) : Character.codePointAt(input, index);
            out.write(hex(0x10000 + Character.codePointAt(input, index)).toCharArray()[0]);
            out.write(hex(c).toCharArray()[0]);
            return Character.isSurrogate(c) ? 2 : 1;
        }
    }
}