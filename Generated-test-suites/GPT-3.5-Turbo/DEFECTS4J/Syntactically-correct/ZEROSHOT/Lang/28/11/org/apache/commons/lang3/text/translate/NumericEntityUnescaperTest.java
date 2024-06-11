import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testTranslate_shouldReturnCorrectCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals('A', writer.toString());
        assertEquals(6, result);
    }
    
    @Test
    public void testTranslate_shouldReturnZeroForInvalidInput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#notanumber;", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_shouldHandleHexadecimalValues() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals('A', writer.toString());
        assertEquals(6, result);
    }
    
    @Test
    public void testTranslate_shouldSkipInvalidHexadecimalValues() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#xnotahex;", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_shouldReturnZeroForInvalidEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&amp;", 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, result);
    }
    
}