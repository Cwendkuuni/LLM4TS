package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[][] {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"}
            }
        );
        StringWriter out = new StringWriter();
        int result = translator.translate("abc", 0, out);
        assertEquals("Translated output", "ABC", out.toString());
        assertEquals("Translated length", 3, result);
    }

    @Test
    public void testTranslate_longestMatch() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[][] {
                {"abc", "ABC"},
                {"ab", "AB"},
                {"a", "A"}
            }
        );
        StringWriter out = new StringWriter();
        int result = translator.translate("abcde", 0, out);
        assertEquals("Translated output", "ABCde", out.toString());
        assertEquals("Translated length", 3, result);
    }

    @Test
    public void testTranslate_noMatch() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[][] {
                {"x", "X"},
                {"y", "Y"},
                {"z", "Z"}
            }
        );
        StringWriter out = new StringWriter();
        int result = translator.translate("abc", 0, out);
        assertEquals("Translated output", "abc", out.toString());
        assertEquals("Translated length", 0, result);
    }

    @Test
    public void testTranslate_edgeCases() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[][] {
                {"", "EMPTY"},
                {"a", "A"},
                {"a", "AA"}
            }
        );
        StringWriter out = new StringWriter();
        int result = translator.translate("", 0, out);
        assertEquals("Translated output", "EMPTY", out.toString());
        assertEquals("Translated length", 0, result);

        out = new StringWriter();
        result = translator.translate("a", 0, out);
        assertEquals("Translated output", "AA", out.toString());
        assertEquals("Translated length", 1, result);
    }
}
