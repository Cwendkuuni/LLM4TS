package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));

        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringWriter out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());

        out.getBuffer().setLength(0);
        result = unescaper.translate("&amp;", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());

        out.getBuffer().setLength(0);
        result = unescaper.translate("&#x41;", 0, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());

        out.getBuffer().setLength(0);
        result = unescaper.translate("&#65", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());

        out.getBuffer().setLength(0);
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        result = unescaper.translate("&#65", 0, out);
        assertEquals(4, result);
        assertEquals("A", out.toString());

        out.getBuffer().setLength(0);
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            result = unescaper.translate("&#65;", 0, out);
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}