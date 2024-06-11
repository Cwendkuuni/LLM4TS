package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_NormalDecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(7, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NormalHexadecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(6, unescaper.translate("&#x41;", 0, writer));
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#invalid;", 0, writer));
    }

    @Test
    public void testTranslate_BoundaryValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(8, unescaper.translate("&#65536;", 0, writer));
        assertEquals(Character.toString(Character.highSurrogate(65536)) + Character.toString(Character.lowSurrogate(65536)), writer.toString());
    }

    @Test
    public void testTranslate_NoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(6, unescaper.translate("&#65", 0, writer));
        assertEquals("A", writer.toString());
    }

}