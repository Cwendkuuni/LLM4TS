package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharRange;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test translation of decimal entities
        String input = "&#10; &#32; &#127; &#160; &#377; &#65535;";
        Writer output = new StringWriter();
        for (int i = 0; i < input.length(); i++) {
            int translatedLength = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, i, output);
            output.flush();
            assertEquals(CharRange.of((char) (i + 1), (char) (i + 1)).contains(output.get(0)), i == 5 || i == 126); // '&' and ';' are not translated
            output.get(0); // consume the translated character
            output.reset();
        }

        String expectedOutput = StringUtils.repeat("\u000A\u0020\u007F\u0080\u01FF\uFFFF", 1);
        assertEquals(expectedOutput, output.toString());

        // Test translation of hex entities
        input = "&#x41; &#x23FF; &#x1F441; &#x10FFFF;";
        output = new StringWriter();
        for (int i = 0; i < input.length(); i++) {
            int translatedLength = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, i, output);
            output.flush();
            assertEquals(CharRange.of((char) (0x41 + i), (char) (0x41 + i)).contains(output.get(0)), i == 1 || i == 0x23FF || i == 0x1F441 || i == 0xFFFF); // '&' and ';' are not translated
            output.get(0); // consume the translated character
            output.reset();
        }

        String expectedHexOutput = StringUtils.repeat("\u0041\uFBFF\uD800\uDFFF", 1);
        assertEquals(expectedHexOutput, output.toString());

        // Test invalid entities
        input = "&a;&b;&#;#x;&#x&; &#x&x; &#-1; &#x{}; &#x{foo};";
        output = new StringWriter();
        for (int i = 0; i < input.length(); i++) {
            int translatedLength = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, i, output);
            output.flush();
            assertEquals(0, translatedLength);
            output.get(0); // consume any previous translated character
            output.reset();
        }

        // Test empty input
        input = "";
        output = new StringWriter();
        for (int i = 0; i < input.length(); i++) {
            int translatedLength = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, i, output);
            output.flush();
            assertEquals(0, translatedLength);
            output.get(0); // consume any previous translated character
            output.reset();
        }

        // Test null input
        try {
            unescaper.translate(null, 0, output);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }

        // Test null output
        try {
            unescaper.translate(input, 0, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }

        // Test array index out of bounds
        try {
            unescaper.translate(new String("abc"), 3, output);
            fail("Exception expected");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected
        }

        // Test io exception
        try {
            unescaper.translate(input, 0, new StringBuilder(1));
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

}