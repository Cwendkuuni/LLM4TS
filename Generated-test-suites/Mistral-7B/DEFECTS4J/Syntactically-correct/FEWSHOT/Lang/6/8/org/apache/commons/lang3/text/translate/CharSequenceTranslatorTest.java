package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.DefaultCharSequenceTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "foo bar baz";
        String expected = "foo%20bar%20baz";

        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslate_Writer() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        String expected = "foo%20bar%20baz";
        String result = writer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslate_NullWriter() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "foo bar baz";
        Writer out = null;

        try {
            translator.translate(input, out);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "";
        String expected = "";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new DefaultCharSequenceTranslator();
        CharSequenceTranslator translator2 = mock(CharSequenceTranslator.class);

        CharSequenceTranslator aggregator = new DefaultCharSequenceTranslator().with(translator1, translator2);

        String input = "foo bar baz";
        StringWriter writer = new StringWriter();

        int consumed1 = 6;
        verify(translator1).translate(input, writer);

        int consumed2 = 12;
        verify(translator2).translate(input, writer);

        aggregator.translate(input, writer);

        String expected = "foo%20bar%20baz";
        String result = writer.toString();
        assertEquals(expected, result);
        verify(translator1).translate(input, writer);
        verify(translator2).translate(input, writer);
    }
}