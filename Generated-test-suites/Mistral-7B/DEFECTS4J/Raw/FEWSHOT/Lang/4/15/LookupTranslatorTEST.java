package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(5, translator.longest);
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateShortInput() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(2, translator.translate(new StringText("ap"), 0, out));
        assertEquals("apple", out.toString());
    }

    @Test
    public void testTranslateLongInput() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(5, translator.translate(new StringText("appel"), 0, out));
        assertEquals("banana", out.toString());
    }

    @Test
    public void testTranslateLongestInput() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(5, translator.translate(new StringText("applebanana"), 0, out));
        assertEquals("banana", out.toString());
    }

    @Test
    public void testTranslatePartialMatch() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new StringText("appl"), 0, out));
        assertEquals("apple", out.toString());
    }

    @Test
    public void testTranslateNoMatch() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new StringText("orange"), 0, out));
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateNullInput() throws IOException {
        CharSequence[] lookup = {new StringText("apple"), new StringText("banana")};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        assertNull(translator.translate(null, 0, out));
    }

    @Test
    public void testTranslateNullLookup() throws IOException {
        StringWriter out = new StringWriter();
        assertNull(new LookupTranslator(null)[0].translate(new StringText("apple"), 0, out));
    }

    @Test
    public void testTranslateEmptyLookup() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]);
        StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(new StringText("apple"), 0, out));
        assertEquals("", out.toString());
    }

    private static class StringText implements CharSequence {

        private final String text;

        public StringText(String text) {
            this.text = text;
        }

        @Override
        public int length() {
            return text.length();
        }

        @Override
        public char charAt(int index) {
            return text.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringText(text.substring(start, end));
        }

        @Override
        public CharSequence toString() {
            return this;
        }
    }
}