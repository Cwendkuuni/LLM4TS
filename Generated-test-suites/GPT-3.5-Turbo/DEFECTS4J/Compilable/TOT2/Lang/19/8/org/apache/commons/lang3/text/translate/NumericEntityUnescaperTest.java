package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_basicDecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_basicHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_missingSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_invalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            unescaper.translate("&#invalid;", 0, writer);
            fail("Expected NumberFormatException was not thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testTranslate_highSurrogatePair() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#128512;", 0, writer);
        assertEquals("\uD83D\uDE00", writer.toString());
    }

}