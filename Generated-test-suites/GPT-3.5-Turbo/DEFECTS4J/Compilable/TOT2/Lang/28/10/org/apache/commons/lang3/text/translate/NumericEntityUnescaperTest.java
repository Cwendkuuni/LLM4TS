package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NormalDecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_NormalHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#", 0, writer));
    }

    @Test
    public void testTranslate_ArrayIndexOutOfBounds() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#", 5, writer));
    }

    // Add more test cases for edge cases and exception scenarios here        

}