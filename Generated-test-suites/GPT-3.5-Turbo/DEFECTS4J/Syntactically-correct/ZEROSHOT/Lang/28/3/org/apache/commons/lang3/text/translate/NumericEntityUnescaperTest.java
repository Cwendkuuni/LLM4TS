import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String input = "&#65;";
        int result = unescaper.translate(input, 0, writer);
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String input = "&#x41;";
        int result = unescaper.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String input = "&;";
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithInvalidHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        String input = "&#x;"; // Invalid hexadecimal input
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}