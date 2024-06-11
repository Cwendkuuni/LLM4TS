package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidDecimalEntity_returnsCorrectTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&amp;#65;", 0, writer);

        assertEquals("Failed to translate valid decimal entity", 6, result);
        assertEquals("Failed to translate valid decimal entity", 'A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withValidHexEntity_returnsCorrectTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals("Failed to translate valid hex entity", 7, result);
        assertEquals("Failed to translate valid hex entity", 'A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withMissingSemicolonAndSemicolonRequiredOption_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&amp#65;", 0, writer);

        assertEquals("Failed to return zero for missing semicolon with semiColonRequired option enabled", 0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withMissingSemicolonAndErrorIfNoSemicolonOption_throwsIllegalArgumentException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();

        unescaper.translate("&amp#65;", 0, writer);
    }

    @Test
    public void testTranslate_withInvalidEntityFormat_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("65;", 0, writer);

        assertEquals("Failed to return zero for invalid entity format", 0, result);
    }

    @Test
    public void testTranslate_withInvalidHexDigit_returnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&amp;#h;", 0, writer);

        assertEquals("Failed to return zero for invalid hex digit", 0, result);
    }

    @Test
    public void testTranslate_withEntityValueAbove65535_returnsTwoChars() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65536;", 0, writer);

        assertEquals("Failed to return correct number of chars for entity value above 65535", 8, result);
        assertEquals("Failed to translate entity value above 65535", '\uD800', writer.toString().charAt(0));
        assertEquals("Failed to translate entity value above 65535", '\uDC00', writer.toString().charAt(1));
    }
}