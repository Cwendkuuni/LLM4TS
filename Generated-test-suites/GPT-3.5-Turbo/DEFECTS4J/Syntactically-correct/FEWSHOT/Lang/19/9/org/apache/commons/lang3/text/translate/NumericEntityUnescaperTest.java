
package org.apache.commons.lang3.text.translate;

import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with decimal numeric entities
        assertEquals(5, unescaper.translate("&#65;", 0, new StringWriter()));
        assertEquals(5, unescaper.translate("&#1234;", 0, new StringWriter()));
        assertEquals(7, unescaper.translate("&#65536;", 0, new StringWriter()));
        assertEquals(8, unescaper.translate("&#655360;", 0, new StringWriter()));
        assertEquals(6, unescaper.translate("&#066;", 0, new StringWriter()));
        assertEquals(6, unescaper.translate("&#00123;", 0, new StringWriter()));

        // Test with hexadecimal numeric entities
        assertEquals(5, unescaper.translate("&#x41;", 0, new StringWriter()));
        assertEquals(5, unescaper.translate("&#x4D2;", 0, new StringWriter()));
        assertEquals(7, unescaper.translate("&#x10000;", 0, new StringWriter()));
        assertEquals(8, unescaper.translate("&#x100000;", 0, new StringWriter()));
        assertEquals(6, unescaper.translate("&#x42;", 0, new StringWriter()));
        assertEquals(6, unescaper.translate("&#x000123;", 0, new StringWriter()));

        // Test with missing semi-colon
        assertEquals(6, unescaper.translate("&#65", 0, new StringWriter()));
        assertEquals(6, unescaper.translate("&#1234", 0, new StringWriter()));
        assertEquals(8, unescaper.translate("&#65536", 0, new StringWriter()));
        assertEquals(9, unescaper.translate("&#655360", 0, new StringWriter()));
        assertEquals(7, unescaper.translate("&#066", 0, new StringWriter()));
        assertEquals(7, unescaper.translate("&#00123", 0, new StringWriter()));

        // Test with no numeric entity
        assertEquals(0, unescaper.translate("ABC", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&ABC;", 0, new StringWriter()));

        // Test with invalid numeric entity
        assertEquals(0, unescaper.translate("&#A;", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#X;", 0, new StringWriter()));
    }
}
