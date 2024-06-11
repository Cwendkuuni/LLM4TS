package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(6, translator.longest);
    }

    @Test
    public void testTranslate() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(6, translator.translate(new String("hello world foo"), 0, out));
        assertEquals("world bar", out.toString());

        out.getBuffered().reset();
        assertEquals(5, translator.translate(new String("foo hello"), 0, out));
        assertEquals("bar world", out.toString());

        out.getBuffered().reset();
        assertNull(translator.translate(new String("foo baz"), 0, out));

        out.getBuffered().reset();
        assertEquals(3, translator.translate(new String("foo"), 0, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateFromMiddle() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(5, translator.translate(new String("hello foo world"), 1, out));
        assertEquals("world bar", out.toString());

        out.getBuffered().reset();
        assertEquals(5, translator.translate(new String("hello foo world"), 4, out));
        assertEquals("world bar", out.toString());

        out.getBuffered().reset();
        assertEquals(3, translator.translate(new String("foo"), 2, out));
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateFromEnd() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(new String("foo world"), 6, out));
        assertEquals("bar", out.toString());

        out.getBuffered().reset();
        assertEquals(3, translator.translate(new String("world foo"), 0, out));
        assertEquals("bar", out.toString());

        out.getBuffered().reset();
        assertNull(translator.translate(new String("baz foo"), 0, out));
    }
}