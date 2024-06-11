package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate() throws IOException {
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateHex() throws IOException {
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        String input = "&;"; // Invalid entity
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslateMultipleEntities() throws IOException {
        String input = "&#65;&#x42;";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
        assertEquals("AB", writer.toString());
    }

    @Test
    public void testTranslateEntityNotFound() throws IOException {
        String input = "ABC"; // No entity
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
    }
}