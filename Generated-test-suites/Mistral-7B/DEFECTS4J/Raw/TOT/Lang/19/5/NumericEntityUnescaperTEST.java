package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link NumericEntityUnescaper} class
 */
public class NumericEntityUnescaperTest {

    private static final NumericEntityUnescaper TESTEE = new NumericEntityUnescaper();
    private static final StringWriter OUT = new StringWriter();

    @Test
    public void testTranslate_ValidEntities_With_Semicolon() {
        CharSequence input = "&nbsp;";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("", OUT.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslate_ValidEntities_Without_Semicolon() {
        CharSequence input = "&xa0";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("\u00A0", OUT.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslate_ValidEntities_Hexadecimal() {
        CharSequence input = "&#x0061;";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("a", OUT.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslate_ValidEntities_OutOfRange() {
        CharSequence input = "&#[x{FFFE}00];";
        int length = TESTEE.translate(input, 0, OUT);
        assertFalse("Expected a failure, but the method returned a non-zero value", length > 0);
        assertEquals("", OUT.toString());
    }

    @Test
    public void testTranslate_ValidEntities_MiddleOfString() {
        CharSequence input = "blah&xa0moo";
        int length = TESTEE.translate(input, 5, OUT);
        assertEquals("\u00A0", OUT.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslate_InvalidEntities_Missing_Ampersand() {
        CharSequence input = "#x0061;";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("", OUT.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_InvalidEntities_Missing_Hash() {
        CharSequence input = "&x0061;";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("", OUT.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_InvalidEntities_Missing_X() {
        CharSequence input = "&#0061;";
        int length = TESTEE.translate(input, 0, OUT);
        assertFalse("Expected a failure, but the method returned a non-zero value", length > 0);
        assertEquals("", OUT.toString());
    }

    @Test
    public void testTranslate_InvalidEntities_Missing_Semicolon() {
        CharSequence input = "&#x0061";
        int length = TESTEE.translate(input, 0, OUT);
        assertFalse("Expected a failure, but the method returned a non-zero value", length > 0);
        assertEquals("", OUT.toString());
    }

    @Test
    public void testTranslate_InvalidEntities_OutOfBounds() {
        CharSequence input = "&#x{FFFFFFFFFFFE}0000;";
        int length = TESTEE.translate(input, 0, OUT);
        assertFalse("Expected a failure, but the method returned a non-zero value", length > 0);
        assertEquals("", OUT.toString());
    }

    @Test
    public void testTranslate_InvalidEntities_NegativeIndex() {
        CharSequence input = "blah";
        int length = TESTEE.translate(input, -1, OUT);
        assertEquals("", OUT.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequence input = "";
        int length = TESTEE.translate(input, 0, OUT);
        assertEquals("", OUT.toString());
        assertEquals(0, length);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testTranslate_Writer_Null() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        int length = translator.translate(new String("test"), 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslate_Input_Null() {
        int length = TESTEE.translate(null, 0, OUT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_InvalidCharacter() {
        CharSequence input = "blah\uffffmoo";
        int length = TESTEE.translate(input, 4, OUT);
        assertEquals(0, length);
    }

    @Test
    public void testTranslate_MultipleEntities() {
        CharSequence input = "blah&xa0moo&nbsp;this&x2013that";
        String expectedOutput = "blaha\u00A0moo \u00A0this—that";
        String output = "";

        int index = 0;
        int length;
        while (index < input.length()) {
            length = TESTEE.translate(input, index, OUT);
            output += OUT.toString();
            index += length;
        }

        assertEquals(expectedOutput, output);
    }
}