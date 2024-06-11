package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65;", 0, writer);
        assertEquals(5, result); // Checks if the translation is correct for ASCII character A
        assertEquals("A", writer.toString()); // Check if the character A is written to the writer
    }

    @Test
    public void testTranslateHexadecimal() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#x41;", 0, writer);
        assertEquals(6, result); // Checks if the translation is correct for hexadecimal ASCII character A
        assertEquals("A", writer.toString()); // Check if the character A is written to the writer
    }

    @Test
    public void testTranslateNonAscii() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#8364;", 0, writer);
        assertEquals(9, result); // Checks if the translation is correct for Unicode character €
        assertEquals("€", writer.toString()); // Check if the character € is written to the writer
    }

    @Test
    public void testTranslateErrorHandling() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#notanumber;", 0, writer);
        assertEquals(0, result); // Checks if the translation fails gracefully for invalid input
        assertEquals("", writer.toString()); // Check if nothing is written to the writer in case of error
    }

    @Test
    public void testTranslateMissingSemicolon() throws IOException {
        StringWriter writer = new StringWriter();
        int result = numericEntityUnescaper.translate("&#65", 0, writer);
        assertEquals(3, result); // Checks if the translation is correct for missing semicolon
        assertEquals("A", writer.toString()); // Check if the character A is written to the writer
    }
}