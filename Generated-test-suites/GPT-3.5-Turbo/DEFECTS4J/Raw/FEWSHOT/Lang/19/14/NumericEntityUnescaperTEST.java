package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        // Test input with decimal numeric entity
        assertEquals(6, unescaper.translate("&#65;", 0, out));
        assertEquals("A", out.toString());

        // Test input with hexadecimal numeric entity
        assertEquals(7, unescaper.translate("&#x41;", 0, out));
        assertEquals("A", out.toString());

        // Test input without numeric entity
        assertEquals(0, unescaper.translate("ABC", 0, out));
        assertEquals("", out.toString());

        // Test input with incomplete numeric entity
        assertEquals(0, unescaper.translate("&#", 0, out));
        assertEquals("", out.toString());

        // Test input with invalid numeric entity
        try {
            assertEquals(13, unescaper.translate("&#xxx;", 0, out));
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Expected
        }
        assertEquals("", out.toString());
    }
}