package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test cases for NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

    private static final NumericEntityUnescaper UNESCAPER = new NumericEntityUnescaper();

    /**
     * Test translate method with valid hex entity.
     */
    @Test
    public void testTranslateHexEntity() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#x1F44D;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
        assertEquals("\uD83E\uDD1D", output.toString());
    }

    /**
     * Test translate method with valid decimal entity.
     */
    @Test
    public void testTranslateDecimalEntity() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#128573;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
        assertEquals("♂", output.toString());
    }

    /**
     * Test translate method with invalid entity.
     */
    @Test
    public void testTranslateInvalidEntity() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#abc;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
        assertEquals("", output.toString());
    }

    /**
     * Test translate method with entity before first character.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateEntityBeforeFirstCharacter() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#x1F44D;a");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }

    /**
     * Test translate method with entity after last character.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateEntityAfterLastCharacter() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("a&amp;#x1F44D;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }

    /**
     * Test translate method with hex entity with wrong format.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateHexEntityWrongFormat() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#x1F4x;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }

    /**
     * Test translate method with decimal entity with wrong format.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateDecimalEntityWrongFormat() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#x1F4a;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }

    /**
     * Test translate method with invalid hex character.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexCharacter() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#x1F0FFF;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }

    /**
     * Test translate method with invalid decimal character.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimalCharacter() {
        CharSequence input = CharSequenceUtils.quotedPrintableEncode("&amp;#256;");
        Writer output = new StringWriter();
        UNESCAPER.translate(input, 0, output);
    }
}