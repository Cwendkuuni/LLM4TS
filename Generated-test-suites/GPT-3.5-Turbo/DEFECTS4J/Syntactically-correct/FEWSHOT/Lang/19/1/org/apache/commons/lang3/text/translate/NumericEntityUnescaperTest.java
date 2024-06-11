
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());

        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#8364;", 0, writer);
        assertEquals(8, result);
        assertEquals("€", writer.toString());

        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#x20AC;", 0, writer);
        assertEquals(9, result);
        assertEquals("€", writer.toString());

        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#99999;", 0, writer);
        assertEquals(8, result);
        assertEquals("\uD869\uDEDF", writer.toString());

        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#;", 0, writer);
        assertEquals(3, result);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}
