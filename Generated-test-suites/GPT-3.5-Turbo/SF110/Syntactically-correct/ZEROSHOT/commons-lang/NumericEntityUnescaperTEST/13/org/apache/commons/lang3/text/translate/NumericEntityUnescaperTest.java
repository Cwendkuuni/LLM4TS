package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidInput_DecodedCharacterWrittenToWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        CharSequence input = "&#65;";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 6", 6, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidInput_NothingWrittenToWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        CharSequence input = "&#99999;";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 0", 0, result);
        assertTrue("Writer should be empty", writer.toString().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NoSemiColon_ThrowsIllegalArgumentException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();

        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_StartsWithX_DecodedCharacterWrittenToWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#x41;";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 6", 6, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }

    @Test
    public void testTranslate_HexValueGreaterThan65535_DecodedCharacterWrittenToWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#65536;";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 8", 8, result);
        assertEquals("Writer should contain the decoded character", "\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_EndsWithSemiColon_SemiColonNotIncludedInResult() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#65;;";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 8", 8, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }

    @Test
    public void testTranslate_SemiColonNotRequired_DecodedCharacterWrittenToWriter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();

        int result = unescaper.translate(input, 0, writer);

        assertEquals("Result should be 5", 5, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }
}