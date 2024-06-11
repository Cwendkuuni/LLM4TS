package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        // Test with decimal 
        assertEquals(6, translator.translate("&#65;", 0, out));
        assertEquals("A", out.toString());

        out.getBuffer().setLength(0);
        assertEquals(7, translator.translate("&#9733;", 0, out));
        assertEquals("★", out.toString());

        // Test with hexadecimal
        out.getBuffer().setLength(0);
        assertEquals(8, translator.translate("&#x41;", 0, out));
        assertEquals("A", out.toString());

        out.getBuffer().setLength(0);
        assertEquals(9, translator.translate("&#x2605;", 0, out));
        assertEquals("★", out.toString());

        // Test with invalid input
        out.getBuffer().setLength(0);
        assertEquals(0, translator.translate("&invalid;", 0, out));
        assertEquals("", out.toString());
    }
}