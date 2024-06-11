package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_HexadecimalNumericEntity_Success() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        unescaper.translate("&#x41;", 0, writer);

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_DecimalNumericEntity_Success() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        unescaper.translate("&#65;", 0, writer);

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#NaN;", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("ABC", 0, writer);

        assertEquals(0, result);
    }

}