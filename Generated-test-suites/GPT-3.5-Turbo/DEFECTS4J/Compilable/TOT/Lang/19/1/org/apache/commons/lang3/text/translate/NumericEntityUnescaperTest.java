package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_WithValidDecimalEntity_ReturnsCorrectCodepoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a &#65; test", 10, writer);
        assertEquals("Result should be 6 (number of characters consumed)", 6, result);
        assertEquals("Resultant string should be 'This is a A test'", "This is a A test", writer.toString());
    }

    @Test
    public void testTranslate_WithValidHexadecimalEntity_ReturnsCorrectCodepoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a &#x41; test", 10, writer);
        assertEquals("Result should be 7 (number of characters consumed)", 7, result);
        assertEquals("Resultant string should be 'This is a A test'", "This is a A test", writer.toString());
    }

    @Test
    public void testTranslate_WithNoSemicolon_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a &#65 test", 10, writer);
        assertEquals("Result should be 0 (no characters consumed)", 0, result);
        assertEquals("Resultant string should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a &#XYZ; test", 10, writer);
        assertEquals("Result should be 0 (no characters consumed)", 0, result);
        assertEquals("Resultant string should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithLargeCodepoint_ReturnsCorrectCodepoint() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a &#1000000; test", 10, writer);
        assertEquals("Result should be 12 (number of characters consumed)", 12, result);
        assertEquals("Resultant string should be a surrogate pair representing the specified codepoint", String.valueOf(Character.toChars(0x10480)), writer.toString());
    }

    @Test
    public void testTranslate_WithEmptyInput_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("", 0, writer);
        assertEquals("Result should be 0 (no characters consumed)", 0, result);
        assertEquals("Resultant string should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithNonEntityInput_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("This is a normal test", 0, writer);
        assertEquals("Result should be 0 (no characters consumed)", 0, result);
        assertEquals("Resultant string should be empty", "", writer.toString());
    }

}