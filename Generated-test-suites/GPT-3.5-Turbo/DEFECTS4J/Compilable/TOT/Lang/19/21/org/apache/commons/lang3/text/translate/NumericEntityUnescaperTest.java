package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Typical use case - decimal entity
        int result = unescaper.translate("&#123;", 0, writer);
        assertEquals(6, result);
        assertEquals('{', writer.toString().charAt(0));

        // Typical use case - hexadecimal entity
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#x7B;", 0, writer);
        assertEquals(7, result);
        assertEquals('{', writer.toString().charAt(0));

        // Typical use case - with optional semicolon
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#123", 0, writer);
        assertEquals(5, result);
        assertEquals('{', writer.toString().charAt(0));

        // Edge case - entity value greater than 0xFFFF
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#65536;", 0, writer);
        assertEquals(11, result);
        assertEquals(0xD800, writer.toString().charAt(0));
        assertEquals(0xDC00, writer.toString().charAt(1));

        // Error scenario - non-numeric entity value
        writer.getBuffer().setLength(0);
        result = unescaper.translate("&#abc;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}