package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_Dec() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals(0, unescaper.translate("&#;", 0, writer));
        assertEquals(0, unescaper.translate("&#97", 0, writer));
        assertEquals(0, unescaper.translate("&#129380;", 0, writer));
        assertEquals(0, unescaper.translate("&d;", 0, writer));
        assertEquals(0, unescaper.translate("&#;", 2, writer));
        assertEquals(9, unescaper.translate("&#97;&#129380;", 0, writer));
        assertEquals(15, unescaper.translate("&#x61;&#x1F600;", 0, writer));
        assertEquals(10, unescaper.translate("abc&#97;&#x61;", 3, writer));
        assertEquals(15, unescaper.translate("&#x61;&#x1F600;", 2, writer));
    }
    
    @Test
    public void testTranslate_Hex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals(0, unescaper.translate("&#;", 0, writer));
        assertEquals(0, unescaper.translate("&#x61", 0, writer));
        assertEquals(0, unescaper.translate("&#x129380;", 0, writer));
        assertEquals(0, unescaper.translate("&x;", 0, writer));
        assertEquals(0, unescaper.translate("&#;", 2, writer));
        assertEquals(9, unescaper.translate("&#x61;&#x129380;", 0, writer));
        assertEquals(15, unescaper.translate("&#x61;&#x1F600;", 0, writer));
        assertEquals(10, unescaper.translate("abc&#x61;&#x61;", 3, writer));
        assertEquals(15, unescaper.translate("&#x61;&#x1F600;", 2, writer));
    }
}