package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaper() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; &lt; &gt; &apos; &quot; &#32; &#x201c; &#x201d; &#128512;";
        StringWriter writer = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        String expected = "& amp; < > ' \" \u0020 \u201c \u201d ප";
        assertEquals(expected, output);
        assertTrue(length > 0);
    }

    @Test
    public void testNumericEntityUnescaperInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&invalidEntity;";
        StringWriter writer = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        String expected = "&invalidEntity;";
        assertEquals(expected, output);
        assertTrue(length == 0);
    }

    @Test
    public void testNumericEntityUnescaperInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#xValidHexEntityInvalid;";
        StringWriter writer = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        String expected = "&#xValidHexEntityInvalid;";
        assertEquals(expected, output);
        assertTrue(length == 0);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumericEntityUnescaperInvalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&&#xNotANumber;";
        StringWriter writer = new StringWriter();
        unescaper.translate(new StringReader(input), 0, writer);
    }

    @Test
    public void testNumericEntityUnescaperEmptyString() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        StringWriter writer = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        assertEquals("", output);
        assertTrue(length == 0);
    }
}