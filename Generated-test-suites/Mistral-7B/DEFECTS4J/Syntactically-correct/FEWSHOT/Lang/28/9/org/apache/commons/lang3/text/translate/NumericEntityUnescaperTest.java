package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidHexEntity() {
        CharSequence input = "&#[xAA]";
        Writer out = new StringWriter();
        int index = 0;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("å", out.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_ValidDecimalEntity() {
        CharSequence input = "&#[133]";
        Writer out = new StringWriter();
        int index = 0;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("s", out.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_ValidHexEntityWithLeadingX() {
        CharSequence input = "&#[xXA3]";
        Writer out = new StringWriter();
        int index = 0;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("Ç", out.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_ValidDecimalEntityWithLeadingZero() {
        CharSequence input = "&#[0123]";
        Writer out = new StringWriter();
        int index = 0;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("i", out.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_InvalidEntity() {
        CharSequence input = "&[foo]";
        Writer out = new StringWriter();
        int index = 0;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_EntityOutsideBounds() {
        CharSequence input = "abc&[def]ghi";
        Writer out = new StringWriter();
        int index = 4; // index is out of bounds

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        assertThrows(IndexOutOfBoundsException.class, () -> translator.translate(input, index, out));
    }

    @Test
    public void testTranslate_EntityWithTrailingSemicolon() {
        CharSequence input = "abc&#[123];def";
        Writer out = new StringWriter();
        int index = 4;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("i", out.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_EntityWithLeadingSemicolon() {
        CharSequence input = "abc;&#[123];def";
        Writer out = new StringWriter();
        int index = 1;

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(input, index, out);

        assertEquals("", out.toString());
        assertEquals(0, length);
    }
}