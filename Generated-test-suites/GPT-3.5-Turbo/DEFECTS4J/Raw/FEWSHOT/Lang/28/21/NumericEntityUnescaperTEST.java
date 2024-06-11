
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    public static final char START_MARKER = '&';
    public static final char EXCLAMATION_MARKER = '#';
    public static final char HEX_PREFIX_MARKER = 'x';
    public static final char SEMICOLON_MARKER = ';';

    @Test
    public void testTranslate_withValidHexEntity_shouldReturnTranslatedValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "&amp;#x41;";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(7, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_withValidDecimalEntity_shouldReturnTranslatedValue() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "&amp;#168;";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(8, result);
        assertEquals("¨", out.toString());
    }

    @Test
    public void testTranslate_withInvalidHexEntity_shouldReturnZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "&amp;#x";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withInvalidDecimalEntity_shouldReturnZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "&amp;#";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withMultipleEntities_shouldTranslateAll() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "&amp;#x41;&amp;#168;";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(15, result);
        assertEquals("A¨", out.toString());
    }

    @Test
    public void testTranslate_withNoEntities_shouldReturnZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = "no entities here";
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withNullWriter_shouldThrowIOException() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        Writer out = null;

        CharSequence input = "&amp;#x41;";
        int index = 0;

        try {
            translator.translate(input, index, out);
        } catch (IOException e) {
            assertEquals("Invalid Writer: null", e.getMessage());
        }
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnZero() throws IOException {
        CharSequenceTranslator translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        CharSequence input = null;
        int index = 0;
        int result = translator.translate(input, index, out);

        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}
