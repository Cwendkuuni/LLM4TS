package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());

        writer = new StringWriter();
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());

        writer = new StringWriter();
        result = unescaper.translate("&#169;", 0, writer);
        assertEquals(7, result);
        assertEquals("©", writer.toString());

        writer = new StringWriter();
        result = unescaper.translate("&#x20ac;", 0, writer);
        assertEquals(9, result);
        assertEquals("€", writer.toString());

        writer = new StringWriter();
        result = unescaper.translate("&#x1f32a;", 0, writer);
        assertEquals(10, result);
        assertEquals("🌪", writer.toString());

        writer = new StringWriter();
        result = unescaper.translate("&amp;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());

    }
}