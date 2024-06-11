package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper();
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertEquals(0, unescaper.translate("", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&a", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#x", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#a", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#xg", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#65536;", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#x10000;", 0, new StringWriter()));
        
        StringWriter writer = new StringWriter();
        assertEquals(2, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());

        StringWriter writer2 = new StringWriter();
        assertEquals(2, unescaper.translate("&#x41;", 0, writer2));
        assertEquals("A", writer2.toString());

        StringWriter writer3 = new StringWriter();
        assertEquals(3, unescaper.translate("&amp;", 0, writer3));
        assertEquals("&", writer3.toString());

        StringWriter writer4 = new StringWriter();
        assertEquals(3, unescaper.translate("&#x26;", 0, writer4));
        assertEquals("&", writer4.toString());
    }
}