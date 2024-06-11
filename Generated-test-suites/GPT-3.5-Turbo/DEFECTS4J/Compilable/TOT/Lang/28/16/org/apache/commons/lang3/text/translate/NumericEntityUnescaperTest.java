package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalNumericEntity_ReturnsCorrectCodePoint() throws IOException {
        String input = "&#65;";
        StringWriter writer = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int result = translator.translate(input, 0, writer);

        assertEquals("A", writer.toString());
        assertEquals(5, result);
    }

    @Test
    public void testTranslate_HexadecimalNumericEntity_ReturnsCorrectCodePoint() throws IOException {
        String input = "&#x1F4A9;";
        StringWriter writer = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int result = translator.translate(input, 0, writer);

        assertEquals("💩", writer.toString());
        assertEquals(9, result);
    }

    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() throws IOException {
        String input = "Hello World";
        StringWriter writer = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int result = translator.translate(input, 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_InvalidNumericEntity_ReturnsZero() throws IOException {
        String input = "&#1234;";
        StringWriter writer = new StringWriter();

        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int result = translator.translate(input, 0, writer);

        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
}

