package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;


public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals("Unexpected result", 6, result);
        assertEquals("Unexpected output", "A", writer.toString());
    }

    @Test
    public void testTranslateWithHexadecimalNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals("Unexpected result", 7, result);
        assertEquals("Unexpected output", "A", writer.toString());
    }

    @Test
    public void testTranslateWithoutEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter writer = new StringWriter();

        int result = unescaper.translate("ABC", 0, writer);
        assertEquals("Unexpected result", 0, result);
        assertEquals("Unexpected output", "", writer.toString());
    }
}