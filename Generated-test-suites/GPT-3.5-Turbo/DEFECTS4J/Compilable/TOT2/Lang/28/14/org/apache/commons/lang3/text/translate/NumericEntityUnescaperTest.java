package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslateBasicDecimal() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#169;", 0, writer);
        assertEquals("Expected translation for &#169;", "\u00A9", writer.toString());
    }

    @Test
    public void testTranslateBasicHex() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("Expected translation for &#x41;", "A", writer.toString());
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#xyz;", 0, writer);
        assertEquals("Expected no translation for invalid entity", 0, writer.toString().length());
    }

    @Test
    public void testTranslateArrayOutOfBounds() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#1234;", 6, writer);
        assertEquals("Expected no translation for array index out of bounds", 0, writer.toString().length());
    }

    @Test
    public void testTranslateIncompleteEntity() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#1234", 0, writer);
        assertEquals("Expected no translation for incomplete entity", 0, writer.toString().length());
    }

}