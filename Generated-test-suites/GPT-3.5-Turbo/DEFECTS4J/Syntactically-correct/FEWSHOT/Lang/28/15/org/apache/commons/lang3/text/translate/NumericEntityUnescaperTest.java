
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        // Test with single decimal digit
        assertEquals(2, unescaper.translate("&amp;#1;", 0, out));
        assertEquals(1, out.toString().length());
        assertEquals('&', out.toString());

        // Test with single hexadecimal digit
        assertEquals(3, unescaper.translate("&amp;#x1;", 0, out));
        assertEquals(1, out.toString().length());
        assertEquals('\u0001', out.toString().charAt(0));

        // Test with multiple decimal digits
        assertEquals(4, unescaper.translate("&amp;#123;", 0, out));
        assertEquals(1, out.toString().length());
        assertEquals('{', out.toString().charAt(0));

        // Test with multiple hexadecimal digits
        assertEquals(5, unescaper.translate("&amp;#xabc;", 0, out));
        assertEquals(1, out.toString().length());
        assertEquals('\u0abc', out.toString().charAt(0));

        // Test with invalid entity value
        assertEquals(0, unescaper.translate("&amp;#", 0, out));
        assertEquals(0, out.toString().length());
    }
}
