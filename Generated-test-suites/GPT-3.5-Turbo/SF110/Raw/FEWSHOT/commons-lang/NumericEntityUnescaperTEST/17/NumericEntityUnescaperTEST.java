package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test valid numeric entity
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());

        // Test valid hexadecimal entity
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#x48;", 0, writer);
        assertEquals(7, result);
        assertEquals("H", writer.toString());

        // Test invalid numeric entity with missing semi-colon
        writer.getBuffer().setLength(0);
        try {
            result = unescaper.translate("&#65", 0, writer);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception thrown as expected
        }
        assertEquals(0, writer.getBuffer().length());

        // Test invalid numeric entity with non-numeric characters
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#1a;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());

        // Test invalid numeric entity with invalid hexadecimal characters
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#xG;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());

        // Test numeric entity greater than 65535
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#50000;", 0, writer);
        assertEquals(9, result);
        assertEquals("\uD187", writer.toString());

        // Test numeric entity less than or equal to 65535
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#100;", 0, writer);
        assertEquals(7, result);
        assertEquals("d", writer.toString());
    }
}