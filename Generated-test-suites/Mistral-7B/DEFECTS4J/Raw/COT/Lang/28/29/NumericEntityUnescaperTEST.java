package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslationWithDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123 entity will be translated.";
        StringWriter output = new StringWriter();

        int lengthBefore = output.length();
        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
        int lengthAfter = output.length();

        assertEquals("The The \u003123 entity will be translated.", input);
        assertEquals("\u003123", CharSequenceUtils.subsequence(input, 5, 7));
        assertEquals("The The 123 entity will be translated.", CharSequenceUtils.quotedPrintableEncode(input));
        assertEquals("The\u00A0The 123 entity will be translated.", CharSequenceUtils.quotedPrintableEncode(output.toString()));
        assertEquals("The\u00A0The 123\u00A0entity will be translated.", output.toString());
        assertEquals(13, lengthAfter - lengthBefore);
    }

    @Test
    public void testTranslationWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &#x41; entity will be translated.";
        StringWriter output = new StringWriter();

        int lengthBefore = output.length();
        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
        int lengthAfter = output.length();

        assertEquals("The The &A; entity will be translated.", input);
        assertEquals("A", CharSequenceUtils.subsequence(input, 5, 6));
        assertEquals("The The &A; entity will be translated.", CharSequenceUtils.quotedPrintableEncode(input));
        assertEquals("The\u00A0The &amp;A;\u00A0 entity will be translated.", CharSequenceUtils.quotedPrintableEncode(output.toString()));
        assertEquals("The\u00A0The A\u00A0 entity will be translated.", output.toString());
        assertEquals(13, lengthAfter - lengthBefore);
    }

    @Test
    public void testTranslationEmpty() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        StringWriter output = new StringWriter();

        int lengthBefore = output.length();
        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
        int lengthAfter = output.length();

        assertEquals("", input);
        assertEquals("", output.toString());
        assertEquals(0, lengthAfter - lengthBefore);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionThrownWhenWriterIsNull() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123 entity will be translated.";
        Writer output = null;

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenInputIsNull() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = null;
        Writer output = new StringWriter();

        unescaper.translate(input, 0, output);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionThrownWhenIndexNegative() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123 entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), -1, output);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionThrownWhenIndexGreaterThanInputLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123 entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 20, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenInputLengthNegative() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = new String(new char[0]);
        StringWriter output = new StringWriter();

        unescaper.translate(input, 0, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenInputLengthZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        StringWriter output = new StringWriter();

        unescaper.translate(input, 0, output);
    }

    @Test
    public void testTranslationWithMalformedEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &amp;#x41; entity will be translated.";
        StringWriter output = new StringWriter();

        int lengthBefore = output.length();
        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
        int lengthAfter = output.length();

        assertEquals("The The &amp;#x41; entity will be translated.", input);
        assertEquals("A", CharSequenceUtils.subsequence(input, 11, 12));
        assertEquals("The The &amp;amp;#x41; entity will be translated.", CharSequenceUtils.quotedPrintableEncode(input));
        assertEquals("The\u00A0The &amp;amp;#A;\u00A0 entity will be translated.", CharSequenceUtils.quotedPrintableEncode(output.toString()));
        assertEquals("The\u00A0The A\u00A0 entity will be translated.", output.toString());
        assertEquals(13, lengthAfter - lengthBefore);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionThrownWhenInvalidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;0xFFF entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionThrownWhenInvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &#xZ entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionThrownWhenInvalidEntityWithLeadingZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &#x0123 entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionThrownWhenInvalidEntityWithoutHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123 entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatExceptionThrownWhenEntityEndsWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "The &num;123; entity will be translated.";
        StringWriter output = new StringWriter();

        unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, output);
    }
}