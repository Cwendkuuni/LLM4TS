package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test case 1
        String input1 = "&#65;";
        StringWriter output1 = new StringWriter();
        int result1 = unescaper.translate(input1, 0, output1);
        assertEquals(4, result1);
        assertEquals("A", output1.toString());

        // Test case 2
        String input2 = "&#x61;";
        StringWriter output2 = new StringWriter();
        int result2 = unescaper.translate(input2, 0, output2);
        assertEquals(5, result2);
        assertEquals("a", output2.toString());

        // Test case 3 (semi-colon required)
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        String input3 = "&#65";
        StringWriter output3 = new StringWriter();
        try {
            unescaper2.translate(input3, 0, output3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }

        // Test case 4 (semi-colon optional)
        NumericEntityUnescaper unescaper3 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input4 = "&#66";
        StringWriter output4 = new StringWriter();
        int result4 = unescaper3.translate(input4, 0, output4);
        assertEquals(4, result4);
        assertEquals("B", output4.toString());

        // Test case 5 (error if no semi-colon)
        NumericEntityUnescaper unescaper4 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input5 = "&#67";
        StringWriter output5 = new StringWriter();
        try {
            unescaper4.translate(input5, 0, output5);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }

    }
}