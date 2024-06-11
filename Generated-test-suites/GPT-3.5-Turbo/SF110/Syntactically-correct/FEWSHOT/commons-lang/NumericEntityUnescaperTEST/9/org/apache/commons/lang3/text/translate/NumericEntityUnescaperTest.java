package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        StringWriter output = new StringWriter();

        // Test translating simple numeric entity
        input.append("&#65;");
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating hex numeric entity
        input.append("&#x41;");
        assertEquals(7, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with semicolon
        input.append("&#65;;");
        assertEquals(7, unescaper.translate(input, 0, output));
        assertEquals("A;", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with invalid character
        input.append("&#ABC;");
        assertEquals(0, unescaper.translate(input, 0, output));
        assertEquals("", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with missing semicolon and semicolon required option
        input.append("&#65");
        assertEquals(0, unescaper.translate(input, 0, output));
        assertEquals("", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with missing semicolon and semicolon optional option
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        input.append("&#65");
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with missing semicolon and error if no semicolon option
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        input.append("&#65");
        try {
            unescaper.translate(input, 0, output);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().equals("Semi-colon required at end of numeric entity"));
        }
        assertEquals("", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with value greater than 65535
        input.append("&#65536;");
        assertEquals(8, unescaper.translate(input, 0, output));
        assertEquals("\uFFFD", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with value less than or equal to 65535
        input.append("&#65;");
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);

        // Test translating numeric entity with value less than or equal to 65535 and semicolon
        input.append("&#65;;");
        assertEquals(7, unescaper.translate(input, 0, output));
        assertEquals("A;", output.toString());
        input.setLength(0);
        output.getBuffer().setLength(0);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}