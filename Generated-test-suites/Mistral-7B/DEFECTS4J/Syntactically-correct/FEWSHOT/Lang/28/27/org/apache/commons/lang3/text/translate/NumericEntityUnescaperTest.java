package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        StringBuilder out = new StringBuilder();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test with valid entities
        CharSequence input = "&amp;&lt;&gt;&apos;&quot;&#32;&#34;&#39;&#35;&#x201c;&#x201d;&#123;&#125;&#160;&#161;&#220;&#223;&#240;&#241;";
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedOutput = "abcDEFгижКLMNOPQRSTUVWXYZ";
        String actualOutput = out.toString();
        assertEquals(expectedOutput, actualOutput);

        // Test with invalid entities
        input = "&";
        index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        assertTrue(out.length() == 0);

        // Test with empty input
        input = "";
        index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        assertTrue(out.length() == 0);

        // Test with null input
        try {
            unescaper.translate(null, 0, out);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null Writer
        try {
            unescaper.translate(input, 0, null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}