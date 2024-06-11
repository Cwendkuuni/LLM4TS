package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Random;

import org.junit.Test;

/**
 * NumericEntityUnescaperTest.java
 *
 * @since 3.0
 */
public class NumericEntityUnescaperTest {

    /**
     * Test for translating a valid numeric entity with decimal base.
     */
    @Test
    public void testTranslateValidNumericEntityDecimalBase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#10;Hello &#32;World! &#137;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "&#10;Hello &#32;World! &#137;";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 1, out.length());
    }

    /**
     * Test for translating a valid numeric entity with hexadecimal base.
     */
    @Test
    public void testTranslateValidNumericEntityHexadecimalBase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#x61;Hello &#x20;World! &#x7F;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "abHello &#x20;World! ff";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 1, out.length());
    }

    /**
     * Test for translating an invalid numeric entity with decimal base.
     */
    @Test
    public void testTranslateInvalidNumericEntityDecimalBase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#a;Hello &#32;World! &#137;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals(input, result);
        assertEquals(lengthBefore, out.length());
    }

    /**
     * Test for translating an invalid numeric entity with hexadecimal base.
     */
    @Test
    public void testTranslateInvalidNumericEntityHexadecimalBase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#xaf;Hello &#32;World! &#137;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals(input, result);
        assertEquals(lengthBefore, out.length());
    }

    /**
     * Test for translating an empty input string.
     */
    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals(input, result);
        assertEquals(lengthBefore, out.length());
    }

    /**
     * Test for translating an input string with characters before and after the numeric entities.
     */
    @Test
    public void testTranslateInputWithCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "Hello &amp;#10;World!";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "Hello \nWorld!";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 2, out.length());
    }

    /**
     * Test for translating an input string with multiple numeric entities.
     */
    @Test
    public void testTranslateMultipleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#x61;Hello &amp;#10;World! &amp;#x7F;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "abHello \nWorld! ff";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 3, out.length());
    }

    /**
     * Test for translating an input string with malformed numeric entities.
     */
    @Test
    public void testTranslateMalformedEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#10Hello &amp;#x123;World! &amp;#137";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "&#10Hello &amp;#x123;World! &#137;";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 1, out.length());
    }

    /**
     * Test for translating an input string with semi-colons after the numeric entities.
     */
    @Test
    public void testTranslateEntitiesWithSemicolons() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#x61;Hello &amp;#10;World! &amp;#x7F;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "abHello \nWorld! ff";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 3, out.length());
    }

    /**
     * Test for translating an input string with numeric entities that are not entities.
     */
    @Test
    public void testTranslateNonEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#10abcd &amp;#x123efgh &amp;#137ijkl";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "&#10abcd &#x123efgh &#137ijkl";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 1, out.length());
    }

    /**
     * Test for translating an input string with entities that are not numeric.
     */
    @Test
    public void testTranslateNonNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#Name &amp;#Symbol &amp;#Other";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "&amp;#Name &amp;#Symbol &amp;#Other";
        assertEquals(expected, result);
        assertEquals(lengthBefore, out.length());
    }

    /**
     * Test for translating an input string with entities that are not well-formed.
     */
    @Test
    public void testTranslateMalformedEntities2() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#x123a &amp;#xz23b &amp;#x123456789abcdef &amp;#x1234567890123456789ABCDEF";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "&#x123a &#xz23b &#x123456789abcdef &#x1234567890123456789ABCDEF";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 1, out.length());
    }

    /**
     * Test for translating an input string with entities that have different casing for 'x'.
     */
    @Test
    public void testTranslateEntitiesWithDifferentxCasing() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        String input = "&amp;#X61;Hello &amp;#x10;World! &amp;#X7F;";

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        String expected = "abHello \nWorld! ff";
        assertEquals(expected, result);
        assertEquals(lengthBefore + input.length() - 3, out.length());
    }
}

class StringReader implements CharSequence {
    private final String string;

    StringReader(String string) {
        this.string = string;
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
        if (start < 0 || end > length() || start > end) {
            throw new IndexOutOfBoundsException();
        }
        if (start == end) {
            return this;
        }
        return new StringSubSequence(this.string, start, end);
    }
}

class StringSubSequence implements CharSequence {

    private final String originalString;
    private final int start;
    private final int end;

    StringSubSequence(String originalString, int start, int end) {
        this.originalString = originalString;
        this.start = start;
        this.end = end;
    }

    @Override
    public int length() {
        return end - start;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException();
        }
        return originalString.charAt(start + index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end > length() || start > end) {
            throw new IndexOutOfBoundsException();
        }
        if (start == end) {
            return this;
        }
        return new StringSubSequence(originalString, start + this.start, end + this.start);
    }
}