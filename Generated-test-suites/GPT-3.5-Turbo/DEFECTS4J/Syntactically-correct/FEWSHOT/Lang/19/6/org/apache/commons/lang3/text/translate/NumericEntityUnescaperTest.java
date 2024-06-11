//Test for translate method
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    private static final int LOW_SURROGATE_MIN = 0xDC00;
    private static final int HIGH_SURROGATE_MIN = 0xD800;
    private static final int HIGH_SURROGATE_MAX = 0xDBFF;
    private static final int LOW_SURROGATE_MAX = 0xDFFF;

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test with alphanumeric entities
        assertEquals(3, unescaper.translate("&65;", 0, writer));
        assertEquals("A", writer.toString());

        // Test with hexadecimal entities
        writer.getBuffer().setLength(0);
        assertEquals(4, unescaper.translate("&#x41;", 0, writer));
        assertEquals("A", writer.toString());

        // Test with hexadecimal entities with lowercase 'x'
        writer.getBuffer().setLength(0);
        assertEquals(4, unescaper.translate("&#x61;", 0, writer));
        assertEquals("a", writer.toString());

        // Test with hexadecimal entities without semicolon
        writer.getBuffer().setLength(0);
        assertEquals(3, unescaper.translate("&#x61", 0, writer));
        assertEquals("a", writer.toString());

        // Test with decimal entities
        writer.getBuffer().setLength(0);
        assertEquals(4, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());

        // Test with hexadecimal surrogate pairs
        writer.getBuffer().setLength(0);
        assertEquals(9, unescaper.translate("&#55357;&#56835;", 0, writer));
        assertEquals("\uD83D\uDE0B", writer.toString());

        // Test with decimal surrogate pairs
        writer.getBuffer().setLength(0);
        assertEquals(9, unescaper.translate("&#55357;&#56835;", 0, writer));
        assertEquals("\uD83D\uDE0B", writer.toString());

        // Test when input is empty
        writer.getBuffer().setLength(0);
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals("", writer.toString());

        // Test when input is null
        writer.getBuffer().setLength(0);
        assertEquals(0, unescaper.translate(null, 0, writer));
        assertEquals("", writer.toString());

        // Test when index is out of bounds
        writer.getBuffer().setLength(0);
        assertEquals(0, unescaper.translate("&#65;", 10, writer));
        assertEquals("", writer.toString());
    }
}