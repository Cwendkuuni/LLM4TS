package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {
    private LookupTranslator translator;

    @Before
    public void setUp() {
        translator = new LookupTranslator(
            new CharSequence[][]{
                {"foo", "bar"},
                {"hello", "world"}
            }
        );
    }

    @Test
    public void testTranslate() throws IOException {
        Writer out = new StringWriter();

        // Test translation of complete match
        int translated = translator.translate("foo", 0, out);
        assertEquals(3, translated);
        assertEquals("bar", out.toString());

        // Test translation of partial match
        out = new StringWriter();
        translated = translator.translate("hello there", 0, out);
        assertEquals(5, translated);
        assertEquals("world there", out.toString());

        // Test translation of multiple matches
        out = new StringWriter();
        translated = translator.translate("foo hello", 0, out);
        assertEquals(3, translated);
        assertEquals("bar", out.toString());

        // Test translation of no match
        out = new StringWriter();
        translated = translator.translate("goodbye", 0, out);
        assertEquals(0, translated);
        assertEquals("", out.toString());
    }
}