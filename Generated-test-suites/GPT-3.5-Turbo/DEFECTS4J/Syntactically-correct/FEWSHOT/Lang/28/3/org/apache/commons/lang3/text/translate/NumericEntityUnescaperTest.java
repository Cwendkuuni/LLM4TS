
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_withDecimalNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withHexadecimalNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#xG;", 0, writer);
        assertEquals(1, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexadecimalNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#xG;", 0, writer);
        assertEquals(1, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withIncompleteNumericEntity() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}
