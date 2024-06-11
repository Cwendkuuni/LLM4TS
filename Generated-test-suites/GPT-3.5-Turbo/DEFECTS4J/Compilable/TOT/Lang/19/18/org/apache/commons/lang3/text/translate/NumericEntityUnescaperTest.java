package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_HexCodepointWithoutSemicolon_SuccessfulTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#x41;", 0, writer);
        
        assertEquals(6, charsTranslated);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_DecimalCodepointWithoutSemicolon_SuccessfulTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#65", 0, writer);
        
        assertEquals(5, charsTranslated);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_HexCodepointWithSemicolon_SuccessfulTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#x41;", 0, writer);
        
        assertEquals(6, charsTranslated);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_DecimalCodepointWithSemicolon_SuccessfulTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#65;", 0, writer);
        
        assertEquals(6, charsTranslated);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexCodepoint_ExceptionCaught() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#xG;", 0, writer);
        
        assertEquals(0, charsTranslated);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalCodepoint_ExceptionCaught() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("&#", 0, writer);
        
        assertEquals(0, charsTranslated);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NotAnEntity_NoTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("ABC", 0, writer);
        
        assertEquals(0, charsTranslated);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EmptyString_NoTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int charsTranslated = unescaper.translate("", 0, writer);
        
        assertEquals(0, charsTranslated);
        assertEquals("", writer.toString());
    }
}