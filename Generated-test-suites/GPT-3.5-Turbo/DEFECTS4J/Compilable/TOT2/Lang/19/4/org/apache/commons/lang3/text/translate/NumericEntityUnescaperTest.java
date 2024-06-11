package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(3, result);
        assertEquals('A', writer.toString().toCharArray()[0]);
    }

    @Test
    public void testTranslate_NormalHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x41;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(4, result);
        assertEquals('A', writer.toString().toCharArray()[0]);
    }

    @Test
    public void testTranslate_NoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(3, result);
        assertEquals('A', writer.toString().toCharArray()[0]);
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#INVALID;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#xINVALID;";
        int index = 0;
        try {
            unescaper.translate(input, index, writer);
            fail("Exception expected");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

}