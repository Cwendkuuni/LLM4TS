package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_noNumericEntity_returnsZero() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "abc";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_invalidNumericEntity_returnsZero() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&;&#;"; // Invalid numeric entities
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_validNumericEntity_returnsEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;"; // Numeric entity for 'A'
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_semiColonRequiredOptionWithoutSemiColon_throwsException() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65"; // Numeric entity without semicolon
        int index = 0;
        unescaper.translate(input, index, writer);
    }

    @Test
    public void testTranslate_semiColonRequiredOptionWithSemiColon_returnsEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65;"; // Numeric entity for 'A'
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_semiColonOptionalOptionWithoutSemiColon_returnsEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65"; // Numeric entity without semicolon
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_errorIfNoSemiColonOptionWithoutSemiColon_throwsException() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        CharSequence input = "&#65"; // Numeric entity without semicolon
        int index = 0;
        unescaper.translate(input, index, writer);
    }

    @Test
    public void testTranslate_hexadecimalEntity_returnsEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#x41;"; // Hexadecimal entity for 'A'
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_unicodeEntity_returnsEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        CharSequence input = "&#9731;"; // Numeric entity for '☃'
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        assertEquals(10, result);
        assertEquals("☃", writer.toString());
    }
}