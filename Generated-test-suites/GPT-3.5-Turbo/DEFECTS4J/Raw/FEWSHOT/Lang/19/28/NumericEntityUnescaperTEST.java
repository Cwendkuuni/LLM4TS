
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with decimal entity
        StringWriter writer1 = new StringWriter();
        assertEquals(6, unescaper.translate("&#102;", 0, writer1));
        assertEquals('f', writer1.toString().charAt(0));

        // Test with hexadecimal entity
        StringWriter writer2 = new StringWriter();
        assertEquals(7, unescaper.translate("&#x1f;", 0, writer2));
        assertEquals('\u001f', writer2.toString().charAt(0));

        // Test without semi-colon at the end
        StringWriter writer3 = new StringWriter();
        assertEquals(7, unescaper.translate("&#x4e", 0, writer3));
        assertEquals('N', writer3.toString().charAt(0));

        // Test without 'x' after '&#'
        StringWriter writer4 = new StringWriter();
        assertEquals(8, unescaper.translate("&#111;", 0, writer4));
        assertEquals('o', writer4.toString().charAt(0));

        // Test with invalid entity
        StringWriter writer5 = new StringWriter();
        assertEquals(0, unescaper.translate("&#ABC;", 0, writer5));
        assertEquals("", writer5.toString());

        // Test when input length is less than 2
        StringWriter writer6 = new StringWriter();
        assertEquals(0, unescaper.translate("&", 0, writer6));
        assertEquals("", writer6.toString());
    }

}
