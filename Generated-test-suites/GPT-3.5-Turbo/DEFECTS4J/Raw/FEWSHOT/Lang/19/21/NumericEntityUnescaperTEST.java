
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer writer = new StringWriter();
        int length = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, length);
        assertEquals("A", writer.toString());

        writer = new StringWriter();
        length = unescaper.translate("&#xa8;", 0, writer);
        assertEquals(7, length);
        assertEquals("¨", writer.toString());

        writer = new StringWriter();
        length = unescaper.translate("&#77", 0, writer);
        assertEquals(4, length);
        assertEquals("", writer.toString());

        writer = new StringWriter();
        length = unescaper.translate("&#x100;", 0, writer);
        assertEquals(8, length);
        assertEquals("Ā", writer.toString());

        writer = new StringWriter();
        length = unescaper.translate("&#x110000;", 0, writer);
        assertEquals(10, length);
        assertEquals("", writer.toString());
    }

}
