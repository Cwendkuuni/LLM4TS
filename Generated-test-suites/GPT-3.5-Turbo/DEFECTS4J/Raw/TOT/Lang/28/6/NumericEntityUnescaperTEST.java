import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateTypicalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        Writer out = new StringWriter();
        unescaper.translate(input, 0, out);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslateHexadecimalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x2665;";
        Writer out = new StringWriter();
        unescaper.translate(input, 0, out);
        assertEquals("♥", out.toString());
    }

    @Test
    public void testTranslateInvalidFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&##;";
        Writer out = new StringWriter();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateInvalidNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#notNumber;";
        Writer out = new StringWriter();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "";
        Writer out = new StringWriter();
        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateOutOfBounds() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#123;";
        Writer out = new StringWriter();
        int result = unescaper.translate(input, input.length(), out);
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}