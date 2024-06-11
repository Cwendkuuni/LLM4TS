package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test class for {@link NumericEntityUnescaper}.
 *
 * @author John Doe
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&amp;".length(), length);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateNumericEntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;#123;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&num;#123;".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 123, output.toCharArray()[0]);
    }

    @Test
    public void testTranslateNumericEntityWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;#123";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&num;#123".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 123, output.toCharArray()[0]);
    }

    @Test
    public void testTranslateHexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x1F44D;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&#x1F44D;".length(), length);
        assertEquals("", output.toString());
        assertEquals('\uD83E' + (char) 0xDD, output.toCharArray()[0]);
    }

    @Test
    public void testTranslateInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;#ABCD";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&num;#ABCD".length(), length);
        assertEquals("", output.toString());
        // This test should print an error message to the console
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals(0, length);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateInputWithMultipleNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;#123;&#x1F44D;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&num;#123;&#x1F44D;".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 123, output.toCharArray()[0]);
        assertEquals('\uD83E' + (char) 0xDD, output.toCharArray()[1]);
    }

    @Test
    public void testTranslateInputWithMixedTextAndNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &num;#123; World &num;#456;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("Hello &num;#123; World &num;#456;".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 123, output.toCharArray()[6]);
        assertEquals((char) 456, output.toCharArray()[10]);
    }

    @Test
    public void testTranslateInputWithNumericEntitiesAtBeginningAndEnd() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;#123;Hello World&num;#456;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("&num;#123;Hello World&num;#456;".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 123, output.toCharArray()[0]);
        assertEquals((char) 456, output.toCharArray()[input.length() - 3]);
    }

    @Test
    public void testTranslateLargeNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "Hello &num;#12345678; World &num;#9876543210;";
        Writer output = new StringWriter();
        int length = unescaper.translate(new StringCharSequence(input), 0, output);
        assertEquals("Hello &num;#12345678; World &num;#9876543210;".length(), length);
        assertEquals("", output.toString());
        assertEquals((char) 12345678, output.toCharArray()[6]);
        assertEquals((char) 9876543210, output.toCharArray()[input.length() - 3]);
    }

    /**
     * Helper class that implements {@link CharSequence} for use with JUnit.
     */
    public static class StringCharSequence implements CharSequence {

        private final String string;

        public StringCharSequence(String str) {
            this.string = str;
        }

        @Override
        public int length() {
            return string.length();
        }

        @Override
        public char charAt(int index) {
            return string.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringCharSequence(string.substring(start, end));
        }

    }

}