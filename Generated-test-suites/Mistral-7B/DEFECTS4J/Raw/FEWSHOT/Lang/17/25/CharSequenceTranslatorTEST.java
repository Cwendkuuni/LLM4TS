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
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = mock(Writer.class);
        CharSequence input = "foo";

        int result = translator.translate(input, 0, out);
        assertEquals(input.length(), result);
        verify(out).write(input.toCharArray());
    }

    @Test
    public void testTranslateWithWriter() {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        Writer out = new StringWriter();
        CharSequence input = "foo";

        translator.translate(input, out);
        assertEquals("foo", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        CharSequence input = "foo";
        Writer out = null;

        translator.translate(input, out);
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identity = (charseq, index, out) -> index + 1;
        CharSequenceTranslator hexTranslator = (charseq, index, out) -> {
            int codepoint = Character.codePointAt(charseq, index);
            String hex = CharSequenceTranslator.hex(codepoint);
            out.write(hex.toCharArray());
            return hex.length();
        };
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { identity, hexTranslator });
        CharSequence input = "foo";
        Writer out = new StringWriter();

        translator.translate(input, 0, out);
        assertEquals("0003FO0033O0033O", out.toString());
    }
}