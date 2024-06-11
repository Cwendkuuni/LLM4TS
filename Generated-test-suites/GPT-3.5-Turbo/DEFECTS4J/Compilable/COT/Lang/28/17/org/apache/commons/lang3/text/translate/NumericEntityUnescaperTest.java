package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Basic test case
        StringBuilder input = new StringBuilder("&#65;");
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        String output = writer.toString();

        assertEquals(6, result);
        assertEquals("A", output);

        // Testing hex input
        input.setLength(0);
        input.append("&#x41;");
        writer.getBuffer().setLength(0);
        result = unescaper.translate(input, 0, writer);
        output = writer.toString();

        assertEquals(7, result);
        assertEquals("A", output);
    }
}
