package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test for valid numeric entities
        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#65;", 0, writer);
            assertEquals(7, translated);
            assertEquals("A", writer.toString());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#x20ac;", 0, writer);
            assertEquals(10, translated);
            assertEquals("€", writer.toString());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        // Test for invalid numeric entities
        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#x0;", 0, writer);
            assertEquals(2, translated);
            assertTrue(writer.toString().isEmpty());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#x;", 0, writer);
            assertEquals(2, translated);
            assertTrue(writer.toString().isEmpty());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#;", 0, writer);
            assertEquals(2, translated);
            assertTrue(writer.toString().isEmpty());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#abc;", 0, writer);
            assertEquals(2, translated);
            assertTrue(writer.toString().isEmpty());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }

        try {
            Writer writer = new StringWriter();
            int translated = unescaper.translate("&#xgg;", 0, writer);
            assertEquals(2, translated);
            assertTrue(writer.toString().isEmpty());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }
}