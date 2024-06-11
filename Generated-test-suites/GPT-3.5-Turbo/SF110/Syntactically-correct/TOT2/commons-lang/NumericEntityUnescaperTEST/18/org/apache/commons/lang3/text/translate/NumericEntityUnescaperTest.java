package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertNotNull(unescaper);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer out = new StringWriter();
        CharSequence input = "&amp;";
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(5, result);
        assertEquals("&", out.toString());
    }

    @Test
    public void testTranslateWithHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer out = new StringWriter();
        CharSequence input = "&#x26;";
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(6, result);
        assertEquals("&", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
     public void testTranslateErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();
        CharSequence input = "&#65";
        int index = 0;
        unescaper.translate(input, index, out);
    }

    @Test
    public void testTranslateInvalidHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer out = new StringWriter();
        CharSequence input = "&#xG;";
        int index = 0;
        int result = unescaper.translate(input, index, out);
        assertEquals(0, result);
    }

}