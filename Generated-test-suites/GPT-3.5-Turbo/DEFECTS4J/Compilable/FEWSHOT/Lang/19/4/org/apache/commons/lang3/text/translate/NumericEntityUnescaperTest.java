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

        // Test with numeric entities of the form &#x?[xX]?\d+;?
        int result1 = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result1);
        assertEquals("A", writer.toString());

        int result2 = unescaper.translate("&#x41;", 0, writer);
        assertEquals(8, result2);
        assertEquals("A", writer.toString());

        int result3 = unescaper.translate("&#97;", 0, writer);
        assertEquals(6, result3);
        assertEquals("a", writer.toString());

        int result4 = unescaper.translate("&#x61;", 0, writer);
        assertEquals(8, result4);
        assertEquals("a", writer.toString());

        // Test with numeric entities that exceed 0xFFFF
        int result5 = unescaper.translate("&#169;", 0, writer);
        assertEquals(7, result5);
        assertEquals("©", writer.toString());

        int result6 = unescaper.translate("&#x2030;", 0, writer);
        assertEquals(10, result6);
        assertEquals("‰", writer.toString());

        // Test with invalid numeric entities
        int result7 = unescaper.translate("&#xyz;", 0, writer);
        assertEquals(0, result7);
        assertEquals("", writer.toString());

    }
}