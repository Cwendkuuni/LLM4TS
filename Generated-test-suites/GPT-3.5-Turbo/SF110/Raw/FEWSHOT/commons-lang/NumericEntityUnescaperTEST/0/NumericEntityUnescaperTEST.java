
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);

        StringBuilder input = new StringBuilder("&amp;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("&", writer.toString());

        input = new StringBuilder("&#x41;&#65;");
        writer = new StringWriter();
        result = unescaper.translate(input, 0, writer);
        assertEquals(13, result);
        assertEquals("AA", writer.toString());
    }

}
