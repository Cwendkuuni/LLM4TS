package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("25", CharSequenceTranslator.hex(35));
        assertNull(CharSequenceTranslator.hex(-1));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate((CharSequence) null);
        assertNull(result);
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(CharSequence.EMPTY);
        assertEquals(CharSequence.EMPTY.toString(), result);
    }

    @Test
    public void testTranslateIdentity() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    @Test
    public void testTranslateTabToSpace() {
        CharSequenceTranslator tabToSpace = new TabToSpaceTranslator();
        String input = "\tfoo";
        String result = tabToSpace.translate(input);
        assertEquals("   foo", result);
    }

    @Test
    public void testTranslateAggregate() {
        CharSequenceTranslator tabToSpace = new TabToSpaceTranslator();
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { tabToSpace, identity });
        String input = "\tfoo";
        String result = translator.translate(input);
        assertEquals("   foo", result);
    }

    @Test
    public void testTranslateWriter() throws IOException {
        CharSequenceTranslator tabToSpace = new TabToSpaceTranslator();
        String input = "\tfoo";
        StringWriter writer = new StringWriter();
        tabToSpace.translate(input, writer);
        String result = writer.toString();
        assertEquals("   foo", result);
    }

    @Test
    public void testTranslateSurrogatePair() throws IOException {
        CharSequenceTranslator dcsNormalizer = new DcsNormalizer();
        String input = "\uD800\uDC00";
        StringWriter writer = new StringWriter();
        dcsNormalizer.translate(input, writer);
        String result = writer.toString();
        assertEquals("\uD834\uDD1E", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator tabToSpace = new TabToSpaceTranslator();
        String input = "\tfoo";
        tabToSpace.translate(input, null);
    }

    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }
    }

    static class TabToSpaceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = Character.codePointCount(input, index, input.length());
            if (len == 1 && Character.isTab(Character.codePointAt(input, index))) {
                out.write(new char[]{' '});
                return 1;
            }
            int consumed = super.translate(input, index, out);
            return consumed;
        }
    }

    static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
            }
            return consumed;
        }
    }
}