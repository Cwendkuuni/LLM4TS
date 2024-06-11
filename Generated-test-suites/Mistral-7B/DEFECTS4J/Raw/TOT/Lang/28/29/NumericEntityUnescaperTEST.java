package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private static final String BAD_INPUT = "&%& &amp; &apos; &quot; &#0; &#x0; &#x7f; &#x{100}; &%#x{ff}; &#{-1}; &%&#x{ff};";
    private static final String GOOD_INPUT_DECIMAL = "&#128; &#160; &#352;";
    private static final String GOOD_INPUT_HEX = "&#x80; &#x200; &#x214;";
    private static final String GOOD_INPUT_MIXED = "&#128; &#x80; &#352; &#x214;";

    @Test
    public void testTranslateDecimalEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();

        String goodInput = GOOD_INPUT_DECIMAL;
        CharSequence input = CharSequenceUtils.subsequence(goodInput, 0, goodInput.length());

        for (int i = 0; i < goodInput.length(); i++) {
            int length = unescaper.translate(input, i, sb);
            assertEquals("Unexpected length of translated output", 2 + (i + 1), length);
        }

        String expectedOutput = "♥   ¶";
        assertEquals("Unexpected translated output", expectedOutput, sb.toString());
    }

    @Test
    public void testTranslateHexEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();

        String goodInput = GOOD_INPUT_HEX;
        CharSequence input = CharSequenceUtils.subsequence(goodInput, 0, goodInput.length());

        for (int i = 0; i < goodInput.length(); i++) {
            int length = unescaper.translate(input, i, sb);
            assertEquals("Unexpected length of translated output", 2 + (i + 1) + (i % 2 == 0 ? 1 : 0), length);
        }

        String expectedOutput = "‚   ƒ";
        assertEquals("Unexpected translated output", expectedOutput, sb.toString());
    }

    @Test
    public void testTranslateMixedEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();

        String goodInput = GOOD_INPUT_MIXED;
        CharSequence input = CharSequenceUtils.subsequence(goodInput, 0, goodInput.length());

        for (int i = 0; i < goodInput.length(); i++) {
            int length = unescaper.translate(input, i, sb);
            assertEquals("Unexpected length of translated output", 2 + (i + 1) + (i % 3 == 0 ? 1 : 0), length);
        }

        String expectedOutput = "♥ ‚ ¶ ƒ";
        assertEquals("Unexpected translated output", expectedOutput, sb.toString());
    }

    @Test
    public void testTranslateInvalidEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();

        CharSequence input = CharSequenceUtils.subsequence(BAD_INPUT, 0, BAD_INPUT.length());
        for (int i = 0; i < BAD_INPUT.length(); i++) {
            int length = unescaper.translate(input, i, sb);
            assertEquals("Unexpected length of translated output", 0, length);
        }

        assertEquals("Unexpected translated output", "", sb.toString());
    }
}