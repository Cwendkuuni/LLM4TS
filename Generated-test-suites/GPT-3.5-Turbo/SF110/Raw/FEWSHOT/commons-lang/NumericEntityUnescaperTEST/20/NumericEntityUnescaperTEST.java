package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter out = new StringWriter();

        int result = unescaper.translate("&#65;", 0, out);
        assertEquals(6, result);
        assertEquals("A", out.toString());

        result = unescaper.translate("&#x41;", 0, out);
        assertEquals(7, result);
        assertEquals("A", out.toString());

        result = unescaper.translate("&amp;", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());

        result = unescaper.translate("&;", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}