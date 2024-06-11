
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate() throws IOException {
        Writer writer = new StringWriter();

        // Test decimal numeric entities
        assertEquals(4, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());

        // Test hexadecimal numeric entities
        writer = new StringWriter();
        assertEquals(5, unescaper.translate("&#x43;", 0, writer));
        assertEquals("C", writer.toString());

        // Test missing semicolon
        writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#65", 0, writer));
        assertEquals("A", writer.toString());

        // Test invalid numeric entity
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&65;", 0, writer));
        assertEquals("", writer.toString());

        // Test invalid hexadecimal entity
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#xG;", 0, writer));
        assertEquals("", writer.toString());
    }
}
