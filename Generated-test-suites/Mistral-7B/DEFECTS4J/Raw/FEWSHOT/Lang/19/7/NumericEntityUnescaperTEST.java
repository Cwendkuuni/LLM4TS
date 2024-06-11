package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidEntities_Hex() {
        String input = "&amp;#x1F600;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("π", output.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslate_ValidEntities_Decimal() {
        String input = "&amp;#128515;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("ℓ", output.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslate_ValidEntities_InvalidHexStart() {
        String input = "&amp;#xX1F600;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        assertThrows(NumberFormatException.class, () -> translator.translate(new StringCharSequence(input), 0, output));
    }

    @Test
    public void testTranslate_ValidEntities_HexTooLong() {
        String input = "&amp;#x1F600AB;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        assertThrows(NumberFormatException.class, () -> translator.translate(new StringCharSequence(input), 0, output));
    }

    @Test
    public void testTranslate_ValidEntities_MixedCaseHex() {
        String input = "&amp;#X1F600;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("π", output.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslate_ValidEntities_DecimalTooLong() {
        String input = "&amp;#1234567890;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        assertThrows(NumberFormatException.class, () -> translator.translate(new StringCharSequence(input), 0, output));
    }

    @Test
    public void testTranslate_ValidEntities_MissingSemicolon() {
        String input = "&amp;#12345;";

        try {
            NumericEntityUnescaper translator = new NumericEntityUnescaper();
            StringWriter output = new StringWriter();
            int length = translator.translate(new StringCharSequence(input), 0, output);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testTranslate_ValidEntities_EmptyInput() {
        String input = "&amp;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("&", output.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslate_ValidEntities_MultipleEntities() {
        String input = "&amp;#x1F600; &amp;#128515;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("πℓ", output.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslate_ValidEntities_LeadingAmpersand() {
        String input = "&amp;#x1F600; &amp;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        int length = translator.translate(new StringCharSequence(input), 0, output);

        assertEquals("π&", output.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslate_InvalidEntities() {
        String input = "&amp;#invalid;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        assertThrows(NumberFormatException.class, () -> translator.translate(new StringCharSequence(input), 0, output));
    }

    @Test
    public void testTranslate_MissingAmpersand() {
        String input = "#x1F600;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter output = new StringWriter();

        assertThrows(IllegalArgumentException.class, () -> translator.translate(new StringCharSequence(input), 0, output));
    }

    @Test
    public void testTranslate_InputNull() {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer output = new StringWriter();

        assertThrows(NullPointerException.class, () -> translator.translate(null, 0, output));
    }

    @Test
    public void testTranslate_OutputNull() {
        String input = "&amp;#x1F600;";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> translator.translate(new StringCharSequence(input), 0, null));
    }
}