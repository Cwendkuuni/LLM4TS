package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter out = new StringWriter();
        String input = "This is a test";

        int result = translator.translate(input, 0, out);
        assertEquals(input, out.toString());
        assertEquals(0, result);
    }

    @Test
    public void testLookupTranslatorWithSingleEntryLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "hello", "hi" } });
        StringWriter out = new StringWriter();
        String input = "hello world";

        int result = translator.translate(input, 0, out);
        assertEquals("hi world", out.toString());
        assertEquals(5, result);
    }

    @Test
    public void testLookupTranslatorWithMultipleEntriesLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "hello", "hi" }, { "world", "earth" } });
        StringWriter out = new StringWriter();
        String input = "hello world";

        int result = translator.translate(input, 0, out);
        assertEquals("hi earth", out.toString());
        assertEquals(5, result);
    }

    @Test
    public void testLookupTranslatorWithLongerInputThanShortestLookupKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "hello", "hi" } });
        StringWriter out = new StringWriter();
        String input = "helloworld";

        int result = translator.translate(input, 0, out);
        assertEquals("hi", out.toString());
        assertEquals(5, result);
    }

    @Test
    public void testLookupTranslatorWithLongerInputThanLongestLookupKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "hello", "hi" } });
        StringWriter out = new StringWriter();
        String input = "helloworldhello";

        int result = translator.translate(input, 0, out);
        assertEquals("hi", out.toString());
        assertEquals(5, result);
    }

    @Test
    public void testLookupTranslatorWithNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "hello", "hi" } });
        StringWriter out = new StringWriter();

        int result = translator.translate(null, 0, out);
        assertNull(out.toString());
        assertEquals(-1, result);
    }

    @Test
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        StringWriter out = new StringWriter();
        String input = "This is a test";

        int result = translator.translate(input, 0, out);
        assertEquals(input, out.toString());
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithEmptyLookupKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "", "hi" } });

        translator.translate("hello", 0, new StringWriter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { null, "hi" } });

        translator.translate("hello", 0, new StringWriter());
    }
}