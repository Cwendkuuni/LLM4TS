package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        assertEquals(0, unescaper.translate("abc", 0, out));
        assertEquals(0, unescaper.translate("&", 0, out));
        assertEquals(0, unescaper.translate("&#", 0, out));
        assertEquals(0, unescaper.translate("&#;", 0, out));
        assertEquals(0, unescaper.translate("&#x", 0, out));
        assertEquals(0, unescaper.translate("&#x;", 0, out));
        assertEquals(0, unescaper.translate("&#x0", 0, out));
        assertEquals(0, unescaper.translate("&#x0;", 0, out));
        assertEquals(0, unescaper.translate("&#x0g;", 0, out));
        assertEquals(0, unescaper.translate("&#g;", 0, out));

        assertEquals(7, unescaper.translate("&#65;", 0, out));
        assertEquals("A", out.toString());
        out.getBuffer().setLength(0);

        assertEquals(7, unescaper.translate("&#x41;", 0, out));
        assertEquals("A", out.toString());
        out.getBuffer().setLength(0);

        assertEquals(8, unescaper.translate("&#x41;x;", 0, out));
        assertEquals("Ax", out.toString());
        out.getBuffer().setLength(0);

        assertEquals(11, unescaper.translate("&#128077;;", 0, out));
        assertEquals("👋", out.toString());
        out.getBuffer().setLength(0);

        assertEquals(8, unescaper.translate("&#128077;x;", 0, out));
        assertEquals("👋x", out.toString());
        out.getBuffer().setLength(0);

        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertEquals(2, unescaper.translate("&#65;", 0, out));
        assertEquals("A", out.toString());
        out.getBuffer().setLength(0);

        assertEquals(4, unescaper.translate("&#65", 0, out));
        assertEquals("A", out.toString());
        out.getBuffer().setLength(0);
    }
}