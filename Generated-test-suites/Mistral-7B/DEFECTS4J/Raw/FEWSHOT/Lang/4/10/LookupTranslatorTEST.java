package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(3, translator.longest);
    }

    @Test
    public void testTranslate_SmallInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        String WriterOutput = "";
        StringWriter writer = new StringWriter();

        String input = "foo";
        int index = 0;
        int length = input.length();

        translator.translate(input, index, writer);
        WriterOutput = writer.getBuffer().toString();
        assertEquals("bar", WriterOutput);
        assertEquals(1, writer.getBuffer().length());

        input = "foobar";
        index = 0;
        translator.translate(input, index, writer);
        WriterOutput = writer.getBuffer().toString();
        assertEquals("barbar", WriterOutput);
        assertEquals(4, writer.getBuffer().length());

        input = "baz";
        index = 0;
        translator.translate(input, index, writer);
        WriterOutput = writer.getBuffer().toString();
        assertEquals("qux", WriterOutput);
        assertEquals(3, writer.getBuffer().length());

        input = "bazqux";
        index = 0;
        translator.translate(input, index, writer);
        WriterOutput = writer.getBuffer().toString();
        assertEquals("quxqux", WriterOutput);
        assertEquals(5, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_InputBiggerThanLookupSize() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "foobarbazqux";
        int index = 0;
        StringWriter writer = new StringWriter();

        translator.translate(input, index, writer);
        String WriterOutput = writer.getBuffer().toString();
        assertEquals("barqux", WriterOutput);
        assertEquals(3, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_InputSmallerThanLookupSize() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "foobar";
        int index = 0;
        StringWriter writer = new StringWriter();

        translator.translate(input, index, writer);
        String WriterOutput = writer.getBuffer().toString();
        assertEquals("bar", WriterOutput);
        assertEquals(1, writer.getBuffer().length());

        index += 3;
        translator.translate(input, index, writer);
        WriterOutput = writer.getBuffer().toString();
        assertEquals("", WriterOutput);
        assertEquals(0, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_NullInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        translator.translate(null, 0, writer);
        assertEquals("", writer.getBuffer().toString());
        assertEquals(0, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_NullLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(null);
        StringWriter writer = new StringWriter();

        String input = "foo";
        int index = 0;
        translator.translate(input, index, writer);
        assertEquals("", writer.getBuffer().toString());
        assertEquals(0, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_EmptyLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]);
        StringWriter writer = new StringWriter();

        String input = "foo";
        int index = 0;
        translator.translate(input, index, writer);
        assertEquals("", writer.getBuffer().toString());
        assertEquals(0, writer.getBuffer().length());
    }

    @Test
    public void testTranslate_InvalidInput() throws Exception {
        CharSequence[][] lookup = {{"foo", "bar"}, {"baz", "qux"}};
        LookupTranslator translator = new LookupTranslator(lookup);

        String input = "foobaz";
        int index = 0;
        StringWriter writer = new StringWriter();

        translator.translate(input, index, writer);
        String WriterOutput = writer.getBuffer().toString();
        assertNull(WriterOutput);
        assertEquals(0, writer.getBuffer().length());
    }
}