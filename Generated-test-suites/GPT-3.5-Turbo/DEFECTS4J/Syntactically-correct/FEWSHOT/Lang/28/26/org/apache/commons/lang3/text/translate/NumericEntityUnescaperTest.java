
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#100;", 0, writer));
        assertEquals("d", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(5, unescaper.translate("&#x41;", 0, writer));
        assertEquals("A", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(3, unescaper.translate("&amp;", 0, writer));
        assertEquals("&", writer.toString());
        
        writer.getBuffer().setLength(0);
        assertEquals(3, unescaper.translate("&lt;", 0, writer));
        assertEquals("<", writer.toString());
    }
}
