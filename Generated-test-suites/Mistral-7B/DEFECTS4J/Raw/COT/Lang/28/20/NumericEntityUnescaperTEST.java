package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;

/**
 * NumericEntityUnescaperTest
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleCase() {
        String input = "&amp;";
        String expectedOutput = "&";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.assertEquals(expectedOutput, output.toString());
        Assert.assertEquals(1, length);
    }

    @Test
    public void testTranslateHexCase() {
        String input = "&#x41;";
        String expectedOutput = "A";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.assertEquals(expectedOutput, output.toString());
        Assert.assertEquals(2, length);
    }

    @Test
    public void testTranslateDecimalCase() {
        String input = "&#101;";
        String expectedOutput = "e";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.assertEquals(expectedOutput, output.toString());
        Assert.assertEquals(2, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        String input = "&invalidEntity;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.fail("Expected IllegalArgumentException");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateArrayIndexOutOfBounds() {
        String input = "abcd&efgh#ijkl";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 9, output);

        Assert.fail("Expected ArrayIndexOutOfBoundsException");
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHex() {
        String input = "&#xzx;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.fail("Expected NumberFormatException");
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimal() {
        String input = "&#xyz;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter output = new StringWriter();
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return CharSequenceUtils.subsequence(input, start, end);
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, output);

        Assert.fail("Expected NumberFormatException");
    }

}