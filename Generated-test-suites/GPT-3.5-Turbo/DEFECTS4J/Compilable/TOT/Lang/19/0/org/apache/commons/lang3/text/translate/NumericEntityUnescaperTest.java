package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper entityUnescaper;

    @Before
    public void setUp() {
        entityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withEmptyInput_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNonNumericEntity_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&amp;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withValidDecimalEntity_returnsExpectedCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidHexEntity_returnsExpectedCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withIncompleteEntity_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNonNumericValueInEntity_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#A;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withOutOfRangeEntity_returnsExpectedCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#1114112;", 0, writer);
        assertEquals(15, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_withNoSemicolonInEntity_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexEntity_returnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = entityUnescaper.translate("&#xG;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}