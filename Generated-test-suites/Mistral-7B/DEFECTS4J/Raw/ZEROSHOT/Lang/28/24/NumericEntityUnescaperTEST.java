package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new java.io.StringWriter();

        // Test simple entity
        String testString1 = "&amp;";
        CharSequence inputSequence1 = CharSequenceUtils.subSequence(testString1, 0, 5);
        int length1 = unescaper.translate(inputSequence1, 0, out);
        assertEquals("Expected length to be 1, actual: " + length1, 1, length1);
        assertEquals("Expected ampersand character to be written, actual: " + out.toString(), "&", out.toString());

        // Test entity with semicolon
        String testString2 = "&amp;#123;";
        CharSequence inputSequence2 = CharSequenceUtils.subSequence(testString2, 0, 7);
        length1 = unescaper.translate(inputSequence2, 0, out);
        assertEquals("Expected length to be 1, actual: " + length1, 1, length1);
        assertEquals("Expected ampersand character to be written, actual: " + out.toString(), "&", out.toString());
        out.flush();
        assertEquals("Expected character '123' to be written, actual: " + out.toString(), "\u007B", out.toString());

        // Test entity with x hexadecimal
        String testString3 = "&amp;#xA1;";
        CharSequence inputSequence3 = CharSequenceUtils.subSequence(testString3, 0, 7);
        length1 = unescaper.translate(inputSequence3, 0, out);
        assertEquals("Expected length to be 1, actual: " + length1, 1, length1);
        assertEquals("Expected ampersand character to be written, actual: " + out.toString(), "&", out.toString());
        out.flush();
        assertEquals("Expected character 'A1' (ALT + 1) to be written, actual: " + out.toString(), "\u00A1", out.toString());

        // Test entity with multiple digits hexadecimal
        String testString4 = "&amp;#x135;";
        CharSequence inputSequence4 = CharSequenceUtils.subSequence(testString4, 0, 8);
        length1 = unescaper.translate(inputSequence4, 0, out);
        assertEquals("Expected length to be 1, actual: " + length1, 1, length1);
        assertEquals("Expected ampersand character to be written, actual: " + out.toString(), "&", out.toString());
        out.flush();
        assertEquals("Expected character 'OS' (Latin capital letter O with stroke) to be written, actual: " + out.toString(), "\u014D", out.toString());

        // Test entity with multiple digits decimal
        String testString5 = "&amp;#1234;";
        CharSequence inputSequence5 = CharSequenceUtils.subSequence(testString5, 0, 7);
        length1 = unescaper.translate(inputSequence5, 0, out);
        assertEquals("Expected length to be 1, actual: " + length1, 1, length1);
        assertEquals("Expected ampersand character to be written, actual: " + out.toString(), "&", out.toString());
        out.flush();
        assertEquals("Expected character '1234' to be written, actual: " + out.toString(), "\u0374", out.toString());

        // Test entity with only number sign
        String testString6 = "&amp;#;";
        CharSequence inputSequence6 = CharSequenceUtils.subsequence(testString6, 0, 5);
        length1 = unescaper.translate(inputSequence6, 0, out);
        assertEquals("Expected length to be 0, actual: " + length1, 0, length1);

        // Test entity with no ampersand
        String testString7 = "#123;";
        CharSequence inputSequence7 = CharSequenceUtils.subsequence(testString7, 0, 4);
        length1 = unescaper.translate(inputSequence7, 0, out);
        assertEquals("Expected length to be 0, actual: " + length1, 0, length1);

        // Test entity with unescaped character
        String testString8 = "&amp;#x41x41x41;";
        CharSequence inputSequence8 = CharSequenceUtils.subsequence(testString8, 0, 10);
        length1 = unescaper.translate(inputSequence8, 0, out);
        assertEquals("Expected length to be 3, actual: " + length1, 3, length1);
        out.flush();
        assertEquals("Expected character 'AAA' to be written, actual: " + out.toString(), "AAA", out.toString());
    }

}