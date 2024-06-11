package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;
    
    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withValidNumericEntity_shouldReturnTranslatedCodepoint() throws IOException {
        StringWriter out = new StringWriter();
        String input = "&#65;";
        int result = unescaper.translate(input, 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_withValidHexNumericEntity_shouldReturnTranslatedCodepoint() throws IOException {
        StringWriter out = new StringWriter();
        String input = "&#x20AC;";
        int result = unescaper.translate(input, 0, out);
        assertEquals(8, result);
        assertEquals("€", out.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldReturnZero() throws IOException {
        StringWriter out = new StringWriter();
        String input = "&#12345678;";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withInvalidHexNumericEntity_shouldReturnZero() throws IOException {
        StringWriter out = new StringWriter();
        String input = "&#xG8;";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withIncompleteNumericEntity_shouldReturnZero() throws IOException {
        StringWriter out = new StringWriter();
        String input = "&#123";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withNoNumericEntity_shouldReturnZero() throws IOException {
        StringWriter out = new StringWriter();
        String input = "ABC";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnZero() throws IOException {
        StringWriter out = new StringWriter();
        String input = "";
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}