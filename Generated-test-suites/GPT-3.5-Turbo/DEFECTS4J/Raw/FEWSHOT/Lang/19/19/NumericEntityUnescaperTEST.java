package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with numeric entity of the form &#xHEX; or &#DEC;
        StringBuilder input1 = new StringBuilder("&#x20;");
        StringBuilder expected1 = new StringBuilder(" ");
        StringBuilder output1 = new StringBuilder();
        int result1 = unescaper.translate(input1, 0, output1);
        assertEquals(expected1.toString(), output1.toString());
        assertEquals(6, result1);

        // Test with numeric entity without semicolon
        StringBuilder input2 = new StringBuilder("&#[65");
        StringBuilder expected2 = new StringBuilder("A");
        StringBuilder output2 = new StringBuilder();
        int result2 = unescaper.translate(input2, 0, output2);
        assertEquals(expected2.toString(), output2.toString());
        assertEquals(4, result2);

        // Test with invalid numeric entity
        StringBuilder input3 = new StringBuilder("&#xinvalid;");
        StringBuilder output3 = new StringBuilder();
        int result3 = unescaper.translate(input3, 0, output3);
        assertEquals(0, result3);
    }
}