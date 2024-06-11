package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Test class for {@link LookupTranslator}
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        // test empty constructor
        LookupTranslator translator = new LookupTranslator();
        // test null lookup
        assertNull(translator.translate(null, 0, new StringWriter()));
        // test empty lookup
        CharSequence[][] lookup = new CharSequence[0][];
        translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "this is a test";
        assertEquals(0, translator.translate(input, 0, out));
        assertEquals("", out.toString());
        // test simple lookup
        lookup = new CharSequence[][] { new CharSequence[] { "test", "hello" } };
        translator = new LookupTranslator(lookup);
        out = new StringWriter();
        input = "test";
        assertEquals(4, translator.translate(input, 0, out));
        assertEquals("hello", out.toString());
        // test multiple lookup
        lookup = new CharSequence[][] { new CharSequence[] { "test", "hello" }, new CharSequence[] { "is", "is" } };
        translator = new LookupTranslator(lookup);
        out = new StringWriter();
        input = "this is a test";
        assertEquals(13, translator.translate(input, 0, out));
        assertEquals("hello is a test", out.toString());
        // test overlapping lookup
        lookup = new CharSequence[][] { new CharSequence[] { "is", "is not" } };
        translator = new LookupTranslator(lookup);
        out = new StringWriter();
        input = "this is a test";
        assertEquals(7, translator.translate(input, 2, out));
        assertEquals("is not", out.toString());
        // test lookup with different length
        lookup = new CharSequence[][] { new CharSequence[] { "is", "is not" }, new CharSequence[] { "test", "hello" } };
        translator = new LookupTranslator(lookup);
        out = new StringWriter();
        input = "this is a test";
        assertEquals(10, translator.translate(input, 1, out));
        assertEquals("is not a test", out.toString());
    }
}