package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;

import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(3, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#invalid;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
    }

    @Test
    public void testTranslateWithHexadecimal() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithLargeEntityValue() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, writer);
        assertEquals(7, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

}