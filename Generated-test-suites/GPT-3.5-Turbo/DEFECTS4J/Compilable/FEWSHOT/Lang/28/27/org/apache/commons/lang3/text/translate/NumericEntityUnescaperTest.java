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

        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals(0, unescaper.translate("&#", 0, writer));
        assertEquals(0, unescaper.translate("&#x", 0, writer));
        assertEquals(0, unescaper.translate("&#123;", 0, writer));
        assertEquals(0, unescaper.translate("&#x123;", 0, writer));
        assertEquals(0, unescaper.translate("&#xg;", 0, writer));
        assertEquals(0, unescaper.translate("&#123", 0, writer));
        assertEquals(0, unescaper.translate("&#123abc;", 0, writer));

        assertEquals(5, unescaper.translate("&amp;", 0, writer));
        assertEquals("&", writer.toString());

        assertEquals(7, unescaper.translate("&#64;", 0, writer));
        assertEquals("@", writer.toString());

        assertEquals(9, unescaper.translate("&#x41;", 0, writer));
        assertEquals("A", writer.toString());

        assertEquals(9, unescaper.translate("&#x43;", 0, writer));
        assertEquals("C", writer.toString());

        assertEquals(11, unescaper.translate("&#x1F604;", 0, writer));
        assertEquals("\uD83D\uDE04", writer.toString());
    }
}