package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.IOException;

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
        CharArrayWriter writer = new CharArrayWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharArrayWriter writer = new CharArrayWriter();
        int result = unescaper.translate("&65;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharArrayWriter writer = new CharArrayWriter();
        unescaper.translate("&#65", 0, writer);
    }

}