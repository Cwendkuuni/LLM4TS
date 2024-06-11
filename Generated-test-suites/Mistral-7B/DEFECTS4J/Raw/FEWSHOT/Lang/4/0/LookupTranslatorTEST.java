package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {
    @Test
    public void testLookupTranslatorEmptyConstructor() {
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
        assertEquals("", translator.translate(new String(""), 0, new StringWriter()));
    }

    @Test
    public void testLookupTranslatorSimpleConstructor() {
        CharSequence[][] lookup = new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("qux"), new String("quux")}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(3, translator.longest);
        StringWriter writer = new StringWriter();

        assertEquals("bar", translator.translate(new String("foo"), 0, writer));
        assertEquals("quux", translator.translate(new String("qux"), 0, writer));
        assertEquals("", translator.translate(new String("baz"), 0, writer));
    }

    @Test
    public void testLookupTranslatorTranslate() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("qux"), new String("quux")}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "foobarquxfoobar";
        int index = 0;

        assertEquals("barquux", translator.translate(new String(input), index, writer).toString());
        index += 3;
        assertEquals("bar", translator.translate(new String(input), index, writer).toString());
        index += 2;
        assertEquals("quux", translator.translate(new String(input), index, writer).toString());
        index += 3;
        assertEquals("", translator.translate(new String(input), index, writer).toString());
    }

    @Test
    public void testLookupTranslatorNullInput() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("qux"), new String("quux")}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = null;

        assertNull(translator.translate(input, 0, writer));
    }

    @Test
    public void testLookupTranslatorIndexOutOfBounds() throws IOException {
        CharSequence[][] lookup = new CharSequence[][] {{new String("foo"), new String("bar")}, {new String("qux"), new String("quux")}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "foobarquxfoobar";
        int index = input.length();

        assertEquals("", translator.translate(new String(input), index, writer).toString());
    }
}