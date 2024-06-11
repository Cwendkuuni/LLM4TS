
package org.apache.commons.lang3.text.translate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withValidStartAndEnd_ReturnsTranslatedEntityAsOutput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidStartHexadecimalAndEnd_ReturnsTranslatedEntityAsOutput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidStartNoSemiColon_ReturnsTranslatedEntityAsOutput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);

        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidStart_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("$&amp;#65;", 1, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNoSemiColonAndSemiColonRequired_ThrowsIllegalArgumentException() throws IOException {
        StringWriter writer = new StringWriter();
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        unescaper.translate("&#65", 0, writer);
    }

    @Test
    public void testTranslate_withInvalidNumberFormat_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#XYZ;", 0, writer);

        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEntityValueGreaterThan65535_ReturnsTranslatedEntityAsOutput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, writer);

        assertEquals(10, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

}
