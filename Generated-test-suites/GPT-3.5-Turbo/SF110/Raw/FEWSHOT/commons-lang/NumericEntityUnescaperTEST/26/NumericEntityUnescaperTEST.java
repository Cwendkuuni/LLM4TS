package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        StringBuilder builder = new StringBuilder();
        Writer writer = new StringWriter(builder);
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals("", builder.toString());

        builder.setLength(0);
        assertEquals(0, unescaper.translate("&", 0, writer));
        assertEquals("", builder.toString());

        builder.setLength(0);
        assertEquals(0, unescaper.translate("&#", 0, writer));
        assertEquals("", builder.toString());

        builder.setLength(0);
        assertEquals(0, unescaper.translate("&#;", 0, writer));
        assertEquals("", builder.toString());

        builder.setLength(0);
        int result = unescaper.translate("&#65;", 0, writer);
        assertTrue(result > 0);
        assertEquals("A", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&amp;#65;", 0, writer);
        assertTrue(result > 0);
        assertEquals("&A", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&amp;#x41;", 0, writer);
        assertTrue(result > 0);
        assertEquals("&A", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#x41;;", 0, writer);
        assertTrue(result > 0);
        assertEquals("A;", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#65;", 0, writer);
        assertTrue(result > 0);
        assertEquals("A", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
        assertEquals("", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#FFFF;", 0, writer);
        assertTrue(result > 0);
        assertEquals("\uDFFF", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#x10000;", 0, writer);
        assertTrue(result > 0);
        assertEquals("\uD800\uDC00", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&amp;#xFFFF;", 0, writer);
        assertTrue(result > 0);
        assertEquals("&\uDFFF", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&amp;#x10000;", 0, writer);
        assertTrue(result > 0);
        assertEquals("&\uD800\uDC00", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#A;", 0, writer);
        assertEquals(0, result);
        assertEquals("", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#256;", 0, writer);
        assertTrue(result > 0);
        assertEquals("Ā", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#x100;", 0, writer);
        assertTrue(result > 0);
        assertEquals("Ā", builder.toString());

        builder.setLength(0);
        result = unescaper.translate("&#256", 0, writer);
        assertEquals(0, result);
        assertEquals("", builder.toString());

        builder.setLength(0);
        NumericEntityUnescaper unescaperWithError = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            result = unescaperWithError.translate("&#65", 0, writer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}