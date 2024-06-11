package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public void testTranslateEmptyInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateShortInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foo";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("bar", out.toString());
    }

    @Test
    public void testTranslateLongInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "hello world";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("world world", out.toString());
    }

    @Test
    public void testTranslateMismatchInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "hello";
        int index = 0;
        translator.translate(input, index, out);
        assertNull(out.toString());
    }

    @Test
    public void testTranslatePartMatchInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "hellooo world";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("world world", out.toString());
    }

    @Test
    public void testTranslatePartMatchInput2() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "hello foo world";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("world bar", out.toString());
    }

    @Test
    public void testTranslatePartMatchInput3() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foo hello world";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("bar hello", out.toString());
    }

    @Test
    public void testTranslatePartMatchInput4() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"hello", "world"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "foo hello world foo";
        int index = 0;
        translator.translate(input, index, out);
        assertEquals("bar hello world bar", out.toString());
    }
}