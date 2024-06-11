package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test cases for NumericEntityUnescaper class
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translate method with valid numeric entity &amp;#x41; (hex)
     */
    @Test
    public void testTranslateHex() {
        String input = "&amp;#x41;";
        String expectedOutput = "A";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int actualLength = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        assertEquals(expectedOutput, output.toString());
        assertEquals(1, actualLength);
    }

    /**
     * Test translate method with valid numeric entity &amp;#161; (decimal)
     */
    @Test
    public void testTranslateDecimal() {
        String input = "&amp;#161;";
        String expectedOutput = "ï";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int actualLength = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        assertEquals(expectedOutput, output.toString());
        assertEquals(1, actualLength);
    }

    /**
     * Test translate method with invalid numeric entity &amp;# (missing numeric value)
     */
    @Test(expected = IOException.class)
    public void testTranslateInvalidEntity1() {
        String input = "&amp;#";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    /**
     * Test translate method with invalid numeric entity &amp;#x (missing numeric value after x)
     */
    @Test(expected = IOException.class)
    public void testTranslateInvalidEntity2() {
        String input = "&amp;#x";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    /**
     * Test translate method with invalid numeric entity &amp;#a (non-hexadecimal character)
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntity3() {
        String input = "&amp;#a";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    /**
     * Test translate method with invalid numeric entity &amp;#0 (negative number)
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidEntity4() {
        String input = "&amp;#0";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    /**
     * Test translate method with invalid numeric entity &amp;# (missing ';' at the end)
     */
    @Test(expected = IOException.class)
    public void testTranslateInvalidEntity5() {
        String input = "&amp;#123";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
    }

    /**
     * Test translate method with empty input
     */
    @Test
    public void testTranslateEmptyInput() {
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int actualLength = translator.translate(CharSequenceUtils.EMPTY, 0, output);
        assertEquals(0, actualLength);
    }

    /**
     * Test translate method with input containing multiple entities
     */
    @Test
    public void testTranslateMultipleEntities() {
        String input = "&amp;#x41;&amp;#161;";
        String expectedOutput = "ABï";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int actualLength = translator.translate(CharSequenceUtils.subSequence(input, 0, input.length()), 0, output);
        assertEquals(expectedOutput, output.toString());
        assertEquals(2, actualLength);
    }
}