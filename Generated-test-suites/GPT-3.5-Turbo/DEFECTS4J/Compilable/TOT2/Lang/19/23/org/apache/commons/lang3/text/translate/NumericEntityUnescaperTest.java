package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.StringWriter;
import java.io.IOException;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_validDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals("Result should be 5", 5, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }
    
    @Test
    public void testTranslate_validHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals("Result should be 5", 5, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }
    
    @Test
    public void testTranslate_missingSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }
    
    @Test
    public void testTranslate_invalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#invalid;", 0, writer);
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }
    
    @Test
    public void testTranslate_highValueEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, writer);
        assertEquals("Result should be 8", 8, result);
        assertEquals("Writer should contain high value character", "\uD800\uDC00", writer.toString());
    }
}