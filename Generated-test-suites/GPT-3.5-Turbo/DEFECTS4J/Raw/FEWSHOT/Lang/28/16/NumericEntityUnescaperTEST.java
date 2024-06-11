package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer;

        // Test case 1: Regular numeric entity
        writer = new StringWriter();
        assertEquals(6, unescaper.translate("&#123;", 0, writer));
        assertEquals(123, writer.toString().charAt(0));

        // Test case 2: Hexadecimal numeric entity
        writer = new StringWriter();
        assertEquals(7, unescaper.translate("&#x1A;", 0, writer));
        assertEquals(26, writer.toString().charAt(0));

        // Test case 3: Entity with leading "x"
        writer = new StringWriter();
        assertEquals(8, unescaper.translate("&#x30;", 0, writer));
        assertEquals(48, writer.toString().charAt(0));

        // Test case 4: Entity with leading "X"
        writer = new StringWriter();
        assertEquals(8, unescaper.translate("&#X414;", 0, writer));
        assertEquals(1044, writer.toString().charAt(0));

        // Test case 5: Multiple entities in the input
        writer = new StringWriter();
        assertEquals(10, unescaper.translate("&#123;&#xABC;", 0, writer));
        assertEquals(123, writer.toString().charAt(0));
        assertEquals(2748, writer.toString().charAt(1));

        // Test case 6: Invalid entity format
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("123;", 0, writer));
        assertEquals("", writer.toString());
    }
}