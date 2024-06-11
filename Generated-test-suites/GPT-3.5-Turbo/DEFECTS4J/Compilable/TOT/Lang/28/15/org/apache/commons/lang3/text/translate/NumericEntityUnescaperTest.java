package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidHexEntity_shouldTranslateToCodepoint() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = translator.translate("&#x41;", 0, writer);
        assertEquals("The translation result is incorrect", 6, result);
        assertEquals("The translated character is incorrect", 'A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withValidDecEntity_shouldTranslateToCodepoint() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = translator.translate("&#65;", 0, writer);
        assertEquals("The translation result is incorrect", 6, result);
        assertEquals("The translated character is incorrect", 'A', writer.toString().charAt(0));
    }

    @Test
    public void testTranslate_withInvalidEntity_shouldNotTranslate() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = translator.translate("&#123;", 0, writer);
        assertEquals("The translation result is incorrect", 0, result);
        assertEquals("The writer should be empty", 0, writer.toString().length());
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnZero() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = translator.translate("", 0, writer);
        assertEquals("The translation result is incorrect", 0, result);
        assertEquals("The writer should be empty", 0, writer.toString().length());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslate_withOutOfBoundsInput_shouldThrowException() throws IOException {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        translator.translate("&#65;", 1, writer);
    }
}