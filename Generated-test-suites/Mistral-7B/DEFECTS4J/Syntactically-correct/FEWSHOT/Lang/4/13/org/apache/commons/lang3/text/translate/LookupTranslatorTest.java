package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(5, translator.longest);
    }

    @Test
    public void testTranslateWithExactMatch() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithSubstringMatch() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "foobar";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateWithNoMatch() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "baz";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithPartialMatch() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "foobarone";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("barone", out.toString());
    }

    @Test
    public void testTranslateWithLongerInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"one", "oneone"}, {"two", "twotwo"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "foobaroneoneoneone";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("baroneoneoneone", out.toString());
    }

    @Test
    public void testTranslateWithLongerLookup() throws Exception {
        CharSequence[][] lookup = {{"foofoofoo", "baz"}, {"oneoneone", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter out = new StringWriter();
        CharSequence input = "foofoofoo";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("baz", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookup() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLookup() {
        new LookupTranslator(new CharSequence[][] {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        CharSequence[][] lookup = {{"", "bar"}};
        new LookupTranslator(lookup);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        CharSequence[][] lookup = {{"foo", null}};
        new LookupTranslator(lookup);
    }
}