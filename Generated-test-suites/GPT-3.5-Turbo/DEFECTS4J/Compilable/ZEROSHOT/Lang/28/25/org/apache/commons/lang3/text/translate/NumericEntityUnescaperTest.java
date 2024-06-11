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

        // Test with numeric entity "&#65;"
        CharSequence input1 = "&amp;#65;";
        int index1 = 5;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals("Translate should return 4", 4, result1);
        assertEquals("Writer should contain 'A'", "A", writer.toString());

        // Test with hex numeric entity "&#x1F4A9;"
        CharSequence input2 = "&amp;#x1F4A9;";
        int index2 = 5;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals("Translate should return 8", 8, result2);
        assertEquals("Writer should contain '💩'", "💩", writer.toString());

        // Test with invalid numeric entity "&#169A"
        CharSequence input3 = "&amp;#169A";
        int index3 = 5;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals("Translate should return 0", 0, result3);
        assertEquals("Writer should be empty", "", writer.toString());
    }
}