package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidInput_ReturnsLength() throws IOException {
        CharSequence input = "&amp;";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(2, result);
    }

    @Test
    public void testTranslate_InvalidInput_ReturnsZero() throws IOException {
        CharSequence input = "&invalid;";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_ValidHexInput_ReturnsLength() throws IOException {
        CharSequence input = "&#x61;";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_ValidDecimalInput_ReturnsLength() throws IOException {
        CharSequence input = "&#97;";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(5, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_SemiColonRequiredAndNoSemiColon_ThrowsException() throws IOException {
        CharSequence input = "&#97";
        StringWriter out = new StringWriter();
        int index = 0;
        unescaper.translate(input, index, out);
    }

    @Test
    public void testTranslate_SemiColonOptionalAndNoSemiColon_ReturnsLength() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharSequence input = "&#97";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(4, result);
    }

    @Test
    public void testTranslate_ErrorIfNoSemiColonAndNoSemiColon_ReturnsLength() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#97";
        StringWriter out = new StringWriter();
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(4, result);
    }

    @Test
    public void testTranslate_HighEntityValue_WritesCorrectChars() throws IOException {
        CharSequence input = "&#65536;";
        StringWriter out = new StringWriter();
        int index = 0;
        unescaper.translate(input, index, out);
        assertEquals("𐀀", out.toString());
    }

    @Test
    public void testTranslate_LowEntityValue_WritesCorrectChar() throws IOException {
        CharSequence input = "&#97;";
        StringWriter out = new StringWriter();
        int index = 0;
        unescaper.translate(input, index, out);
        assertEquals("a", out.toString());
    }
}