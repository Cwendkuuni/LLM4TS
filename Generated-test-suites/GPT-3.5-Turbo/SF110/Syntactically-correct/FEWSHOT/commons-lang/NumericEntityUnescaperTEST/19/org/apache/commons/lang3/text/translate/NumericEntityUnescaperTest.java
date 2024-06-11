
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    }

    @Test
    public void testIsSet() {
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_ValidHexadecimalEntity_ReturnsTranslatedCharacter() throws IOException {
        Writer writer = new StringWriter();
        assertEquals(6, unescaper.translate("&#x0061;", 0, writer));
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslate_ValidDecimalEntity_ReturnsTranslatedCharacter() throws IOException {
        Writer writer = new StringWriter();
        assertEquals(7, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslate_NoSemiColonRequired_ReturnsTranslatedCharacter() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        Writer writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#x0061", 0, writer));
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslate_NoSemiColon_ReturnsZero() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#97", 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity_ReturnsZero() throws IOException {
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#xG;", 0, writer));
        assertEquals("", writer.toString());
    }
}
