package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#32; &#128; &#x7F";
        int index = 0;

        int result = unescaper.translate(input, index, out);
        assertEquals("&amp; &lt; &gt; ' ' ¬", bytes.toString());
        assertEquals(8, result);
        index += 8;

        result = unescaper.translate(input, index, out);
        assertEquals("", bytes.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslateInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&invalid";
        int index = 0;

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, index, out));
    }

    @Test
    public void testTranslateHexEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&#[x41]&#[x52]";
        int index = 0;

        int result = unescaper.translate(input, index, out);
        assertEquals("A", bytes.toString());
        assertEquals(2, result);
        index += 2;

        result = unescaper.translate(input, index, out);
        assertEquals("B", bytes.toString());
        assertEquals(1, result);
        index += 1;
    }

    @Test
    public void testTranslateInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&#[xZ]";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> unescaper.translate(input, index, out));
    }

    @Test
    public void testTranslateSingleCharacterEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&eacute; &nbsp; &iexcl;";
        int index = 0;

        int result = unescaper.translate(input, index, out);
        assertEquals("À", bytes.toString());
        assertEquals(1, result);
        index += 1;

        result = unescaper.translate(input, index, out);
        assertEquals(" ", bytes.toString());
        assertEquals(1, result);
        index += 1;

        result = unescaper.translate(input, index, out);
        assertEquals("¡", bytes.toString());
        assertEquals(1, result);
        index += 1;
    }

    @Test
    public void testTranslateMultipleCharacterEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&copy; &trade; &reg;";
        int index = 0;

        int result = unescaper.translate(input, index, out);
        assertEquals("©", bytes.toString());
        assertEquals(3, result);
        index += 3;

        result = unescaper.translate(input, index, out);
        assertEquals("®", bytes.toString());
        assertEquals(1, result);
        index += 1;

        result = unescaper.translate(input, index, out);
        assertEquals("™", bytes.toString());
        assertEquals(1, result);
        index += 1;
    }

    @Test
    public void testTranslateLongEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        when(out.write(anyInt())).then(i -> bytes.write((int) i.byteValue()));

        CharSequence input = "&#[x2212;];";
        int index = 0;

        int result = unescaper.translate(input, index, out);
        assertEquals("-", bytes.toString());
        assertEquals(3, result);
        index += 3;
    }
}