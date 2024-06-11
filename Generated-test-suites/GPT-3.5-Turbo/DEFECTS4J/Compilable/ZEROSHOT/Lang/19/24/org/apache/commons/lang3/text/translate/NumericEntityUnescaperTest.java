package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithOptionalSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateHexadecimalWithOptionalSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x41";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateInvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#INVALID;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}