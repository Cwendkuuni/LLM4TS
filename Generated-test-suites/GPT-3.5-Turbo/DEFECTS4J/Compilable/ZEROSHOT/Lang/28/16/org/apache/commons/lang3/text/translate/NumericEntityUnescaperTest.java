package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        // Test with valid input
        CharSequence input1 = "&#65;";
        int index1 = 0;
        int result1 = unescaper.translate(input1, index1, writer);
        assertEquals(4, result1);
        assertEquals("A", writer.toString());

        // Test with hexadecimal input
        CharSequence input2 = "&#x41;";
        int index2 = 0;
        int result2 = unescaper.translate(input2, index2, writer);
        assertEquals(5, result2);
        assertEquals("A", writer.toString());

        // Test with invalid input
        CharSequence input3 = "&#";
        int index3 = 0;
        int result3 = unescaper.translate(input3, index3, writer);
        assertEquals(0, result3);
        assertEquals("", writer.toString());
    }
}