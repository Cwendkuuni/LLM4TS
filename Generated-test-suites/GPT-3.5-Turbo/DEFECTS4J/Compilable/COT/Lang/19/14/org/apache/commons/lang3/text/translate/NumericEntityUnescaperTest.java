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
        
        // Test basic functionality
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals(0, unescaper.translate("&123;", 0, writer));
        assertEquals(7, unescaper.translate("&#65;", 0, writer));
        assertEquals(7, unescaper.translate("&#x41;", 0, writer));
        
        // Test edge cases
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals(0, unescaper.translate("&;", 0, writer));
        assertEquals(0, unescaper.translate("&#;", 0, writer));
        assertEquals(0, unescaper.translate("&#x;", 0, writer));
        assertEquals(0, unescaper.translate("&1;", 0, writer));
        
        // Test exception handling
        assertEquals(0, unescaper.translate("&#xg;", 0, writer));
        assertEquals(0, unescaper.translate("&#x1234567890;", 0, writer));
    }
}