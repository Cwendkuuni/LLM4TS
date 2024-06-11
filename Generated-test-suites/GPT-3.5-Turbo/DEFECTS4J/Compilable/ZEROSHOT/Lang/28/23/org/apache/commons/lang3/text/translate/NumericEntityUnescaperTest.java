package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NumberEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_HexNumberEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidNumberEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_InvalidHexNumberEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NonNumberEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&amp;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_MultipleEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;&#x42;", 0, writer);
        assertEquals(8, result);
        assertEquals("AB", writer.toString());
    }
}