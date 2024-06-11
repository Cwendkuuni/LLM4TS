package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidInput_shouldTranslateCorrectly() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65;";
        Writer writer = new StringWriter();

        translator.translate(input, 0, writer);

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidHexInput_shouldTranslateCorrectly() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#x41;";
        Writer writer = new StringWriter();

        translator.translate(input, 0, writer);

        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withMissingSemicolon_shouldNotTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65"; // missing semicolon
        Writer writer = new StringWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withInvalidHexInput_shouldNotTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#xG1;"; // invalid hex character
        Writer writer = new StringWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withInvalidDecimalInput_shouldNotTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#13A;"; // invalid decimal character
        Writer writer = new StringWriter();

        int result = translator.translate(input, 0, writer);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_withOutOfRangeValue_shouldTranslateSurrogatePair() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65536;"; // out of range value
        Writer writer = new StringWriter();

        translator.translate(input, 0, writer);

        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_withValidInputButInvalidOffset_shouldNotTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        String input = "&#65;";
        Writer writer = new StringWriter();

        int result = translator.translate(input, 3, writer); // invalid offset

        assertEquals(0, result);
    }
}