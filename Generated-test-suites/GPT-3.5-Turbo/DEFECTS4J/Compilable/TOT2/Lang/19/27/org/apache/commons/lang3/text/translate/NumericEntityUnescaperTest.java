package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_NormalNumericEntity() throws Exception {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_HexadecimalNumericEntity() throws Exception {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_EntityValueOverFFFF() throws Exception {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#187;", 0, writer);
        assertEquals("»", writer.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntityFormat() throws Exception {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#notanumber;", 0, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NoSemicolonAtEnd() throws Exception {
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65", 0, writer);
        assertEquals("A", writer.toString());
    }

    // Add more test cases as needed

}