package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * NumericEntityUnescaperTest.java
 */
public class NumericEntityUnescaperTest {

    private static final String TEST_CASE_01 = "&nbsp;";
    private static final String TEST_CASE_02 = "&nbsp;";
    private static final String TEST_CASE_03 = "&nbsp;";
    private static final String TEST_CASE_04 = "&iexcl;";
    private static final String TEST_CASE_05 = "&cent;";
    private static final String TEST_CASE_06 = "&pound;";
    private static final String TEST_CASE_07 = "&curren;";
    private static final String TEST_CASE_08 = "&yen;";
    private static final String TEST_CASE_09 = "&euro;";
    private static final String TEST_CASE_10 = "&copy;";
    private static final String TEST_CASE_11 = "&reg;";
    private static final String TEST_CASE_12 = "&trade;";
    private static final String TEST_CASE_13 = "&times;";
    private static final String TEST_CASE_14 = "&divide;";
    private static final String TEST_CASE_15 = "&plusmn;";
    private static final String TEST_CASE_16 = "&sup1;";
    private static final String TEST_CASE_17 = "&sup2;";
    private static final String TEST_CASE_18 = "&sup3;";
    private static final String TEST_CASE_19 = "&sup4;";
    private static final String TEST_CASE_20 = "&sup5;";
    private static final String TEST_CASE_21 = "&sup6;";
    private static final String TEST_CASE_22 = "&sup7;";
    private static final String TEST_CASE_23 = "&sup8;";
    private static final String TEST_CASE_24 = "&sup9;";
    private static final String TEST_CASE_25 = "&deg;";
    private static final String TEST_CASE_26 = "&middot;";
    private static final String TEST_CASE_27 = "&micro;";
    private static final String TEST_CASE_28 = "&para;";
    private static final String TEST_CASE_29 = "&macr;";
    private static final String TEST_CASE_30 = "&oX2019;"; // single right quotation mark
    private static final String TEST_CASE_31 = "&oX201C;"; // left double quotation mark
    private static final String TEST_CASE_32 = "&oX201D;"; // right double quotation mark
    private static final String TEST_CASE_33 = "&oX2032;"; // per mille sign
    private static final String TEST_CASE_34 = "&oX2033;"; // pilcrow sign
    private static final String TEST_CASE_35 = "&oX2034;"; // single low-9 quotation mark
    private static final String TEST_CASE_36 = "&oX2035;"; // double low-9 quotation mark
    private static final String TEST_CASE_37 = "&OX2122;"; // integral sign
    private static final String TEST_CASE_38 = "&OX2151;"; // infinity sign
    private static final String TEST_CASE_39 = "&OX2153;"; // downward arrow
    private static final String TEST_CASE_40 = "&OX2211;"; // summation sign
    private static final String TEST_CASE_41 = "&OX2212;"; // minus sign
    private static final String TEST_CASE_42 = "&OX2248;"; // not sign
    private static final String TEST_CASE_43 = "&OX2260;"; // less-than sign
    private static final String TEST_CASE_44 = "&OX2261;"; // greater-than sign
    private static final String TEST_CASE_45 = "&OX2265;"; // equal sign
    private static final String TEST_CASE_46 = "&OX2266;"; // not equal sign
    private static final String TEST_CASE_47 = "&OX226B;"; // approximate equal sign
    private static final String TEST_CASE_48 = "&OX226F;"; // identification dot

    @Test
    public void testSimpleNumericEntities() {
        String input = "This text contains some &nbsp; entities.";
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
                return input.subSequence(start, end);
            }
        }, 0, output);
        assertEquals(input.length() + 16, length);
        String expectedOutput = "This text contains some   entities.";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testHexNumericEntities() {
        String input = "This text contains some &#x00A0; entities.";
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
                return input.subSequence(start, end);
            }
        }, 0, output);
        assertEquals(input.length() + 16, length);
        String expectedOutput = "This text contains some   entities.";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testNumericEntitiesWithInvalidSyntax() {
        String input = "This text contains some &# invalid syntax; entities.";
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
                return input.subSequence(start, end);
            }
        }, 0, output);
        assertEquals(input.length(), length);
        assertTrue(output.toString().isEmpty());
    }

    @Test
    public void testNumericEntitiesInsideString() {
        String input = "This text contains some &copy;2016 entities.";
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
                return input.subSequence(start, end);
            }
        }, 0, output);
        assertEquals(input.length() + 2, length);
        String expectedOutput = "This text contains some ©2016 entities.";
        assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testTranslationOfSimpleNumericEntities() {
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        for (String entity : new String[] { TEST_CASE_01, TEST_CASE_02, TEST_CASE_03, TEST_CASE_04, TEST_CASE_05, TEST_CASE_06, TEST_CASE_07, TEST_CASE_08, TEST_CASE_09, TEST_CASE_10, TEST_CASE_11, TEST_CASE_12, TEST_CASE_13, TEST_CASE_14, TEST_CASE_15, TEST_CASE_16, TEST_CASE_17, TEST_CASE_18, TEST_CASE_19, TEST_CASE_20, TEST_CASE_21, TEST_CASE_22, TEST_CASE_23, TEST_CASE_24, TEST_CASE_25, TEST_CASE_26, TEST_CASE_27, TEST_CASE_28, TEST_CASE_29, TEST_CASE_30, TEST_CASE_31, TEST_CASE_32, TEST_CASE_33, TEST_CASE_34, TEST_CASE_35, TEST_CASE_36, TEST_CASE_37, TEST_CASE_38, TEST_CASE_39, TEST_CASE_40, TEST_CASE_41, TEST_CASE_42, TEST_CASE_43, TEST_CASE_44, TEST_CASE_45, TEST_CASE_46, TEST_CASE_47, TEST_CASE_48 }) {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return entity.length();
                }

                @Override
                public char charAt(int index) {
                    return entity.charAt(index);
                }

                @Override
                public CharSequence subSequence(int start, int end) {
                    return entity.subSequence(start, end);
                }
            }, 0, output);
            if (entity.startsWith("&") && entity.endsWith(";")) {
                int expectedLength = entity.length() + 2;
                assertEquals(expectedLength, length);
                char[] expectedChars = Character.toChars(entity.charAt(1) == 'x' ? Integer.parseInt(entity.substring(2, entity.length() - 1), 16) : Integer.parseInt(entity.substring(2, entity.length() - 1), 10));
                assertArrayEquals(expectedChars, output.toString().toCharArray());
            } else {
                assertTrue(output.toString().isEmpty());
            }
        }
    }

    @Test
    public void testTranslationOfComplexNumericEntities() {
        Writer output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This text contains &copy;&#xA9;2016 entities.";
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
                return input.subSequence(start, end);
            }
        }, 0, output);
        assertEquals(input.length() + 3, length);
        String expectedOutput = "This text contains ©&#xA9;2016 entities.";
        assertEquals(expectedOutput, output.toString());
    }
}