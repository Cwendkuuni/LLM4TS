
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // test for decimal numeric entities
        assertEquals(8, unescaper.translate("&#38;", 0, new StringWriter()));
        assertEquals(9, unescaper.translate("&#9;", 0, new StringWriter()));
        assertEquals(97, unescaper.translate("&#97;", 0, new StringWriter()));
        assertEquals(122, unescaper.translate("&#122;", 0, new StringWriter()));

        // test for hexadecimal numeric entities
        assertEquals(10, unescaper.translate("&#xa;", 0, new StringWriter()));
        assertEquals(65, unescaper.translate("&#x41;", 0, new StringWriter()));
        assertEquals(98, unescaper.translate("&#x62;", 0, new StringWriter()));
        assertEquals(102, unescaper.translate("&#x66;", 0, new StringWriter()));

        // test for mixed numeric entities
        assertEquals(38, unescaper.translate("&#x26;", 0, new StringWriter()));
        assertEquals(10, unescaper.translate("&#xa;", 0, new StringWriter()));
        assertEquals(97, unescaper.translate("&#97;", 0, new StringWriter()));
        assertEquals(122, unescaper.translate("&#x7a;", 0, new StringWriter()));
    }
}
