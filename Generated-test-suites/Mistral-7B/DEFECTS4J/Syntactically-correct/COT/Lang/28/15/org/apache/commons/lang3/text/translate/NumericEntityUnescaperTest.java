package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Tests for {@link NumericEntityUnescaper} class.
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translate method with valid hex entity.
     */
    @Test
    public void testTranslateHexEntity() {
        String input = "&#x41;&#x42;";
        StringWriter output = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, output);
        assertEquals("AB", output.toString());
        assertEquals(3, length);
    }

    /**
     * Test translate method with valid decimal entity.
     */
    @Test
    public void testTranslateDecimalEntity() {
        String input = "&#10;&#13;";
        StringWriter output = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, output);
        assertEquals("\n\r", output.toString());
        assertEquals(3, length);
    }

    /**
     * Test translate method with invalid entity.
     */
    @Test
    public void testTranslateInvalidEntity() {
        String input = "&#xyz;";
        StringWriter output = new StringWriter();
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new StringReader(input), 0, output));
    }

    /**
     * Test translate method with entity outside the input sequence.
     */
    @Test
    public void testTranslateEntityOutsideInput() {
        String input = "abc&def";
        StringWriter output = new StringWriter();
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new StringReader(input), 5, output));
    }

    /**
     * Test translate method with entity with non-numeric characters.
     */
    @Test
    public void testTranslateEntityWithNonNumeric() {
        String input = "&#xyzABC;";
        StringWriter output = new StringWriter();
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new StringReader(input), 0, output));
    }

    /**
     * Test negative array indices in subSequence call.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeSubSequenceIndex() {
        String input = "&#x41;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        unescaper.translate(new StringReader(input), 0, new StringWriter());
        unescaper.untranslate(new StringReader("AB"), 0, new StringWriter(), false); // Call untranslate method to reset state
        unescaper.translate(new StringReader(input), -1, new StringWriter());
    }

    /**
     * Test null input.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() {
        StringWriter output = new StringWriter();
        unescaper.translate(null, 0, output);
    }

    /**
     * Test null output.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullOutput() {
        String input = "&amp;";
        unescaper.translate(new StringReader(input), 0, null);
    }

    /**
     * Test null reader.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullReader() {
        StringWriter output = new StringWriter();
        unescaper.translate(null, 0, output);
    }
}