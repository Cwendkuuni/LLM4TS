package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withEmptyInput_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withIndexGreaterThanOrEqualToSequenceLength_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#123;", 7, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withNoHash_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&abc;", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withNoSemicolon_afterHash_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#123", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withInvalidHexadecimalNumber_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#xZZ;", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withValidHexadecimalNumber_returnsCodePoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidDecimalNumber_returnsCodePoint() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidDecimalNumber_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#123abc;", 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withEntityValueGreaterThanFFFF_returnsSupplementaryCodePoints() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#128514;", 0, writer);

        assertEquals(11, result);
        assertEquals("😂", writer.toString());
    }
}