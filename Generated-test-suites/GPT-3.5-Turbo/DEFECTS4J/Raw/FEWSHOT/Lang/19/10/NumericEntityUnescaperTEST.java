
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_indexReturnsCorrectValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(12, unescaper.translate("&#x9;", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#x9;", 1, new StringWriter()));
    }
    
    @Test
    public void testTranslate_inputLengthReturnsCorrectValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertEquals(5, unescaper.translate("&#x9;", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("", 0, new StringWriter()));
    }
    
    @Test
    public void testTranslate_validHexadecimalCharacterTranslatesCorrectly() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        unescaper.translate("&#x9;", 0, writer);
        assertEquals("\t", writer.toString());
    }
    
    @Test
    public void testTranslate_validDecimalCharacterTranslatesCorrectly() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        unescaper.translate("&#9;", 0, writer);
        assertEquals("\t", writer.toString());
    }
    
    @Test
    public void testTranslate_invalidFormatReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#q;", 0, writer));
    }
    
    @Test
    public void testTranslate_invalidNumberFormatReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#xq;", 0, writer));
    }
    
    @Test
    public void testTranslate_invalidCharacterCodeReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#999999;", 0, writer));
    }
    
    @Test
    public void testTranslate_characterCodeGreaterThan0xFFFFReturnsTwoChars() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        unescaper.translate("&#65536;", 0, writer);
        assertEquals("\uD800\uDC00", writer.toString());
    }
    
    @Test
    public void testTranslate_characterCodeLessThanOrEqualTo0xFFFFReturnsOneChar() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        unescaper.translate("&#234;", 0, writer);
        assertEquals("Ȋ", writer.toString());
    }
    
}
