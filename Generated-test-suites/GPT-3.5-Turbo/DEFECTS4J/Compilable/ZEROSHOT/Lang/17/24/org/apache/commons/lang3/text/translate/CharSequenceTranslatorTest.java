package org.apache.commons.lang3.text.translate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Test
    public void testTranslate_WhenInputIsNull_ReturnNull() {
        CharSequence input = null;
        String expectedOutput = null;

        String actualOutput = translator.translate(input);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslate_WhenInputIsEmpty_ReturnEmptyString() {
        CharSequence input = "";
        String expectedOutput = "";

        String actualOutput = translator.translate(input);

        assertEquals(expectedOutput, actualOutput);
    }

    // Add more test cases to comprehensively test the translate method

    @Test
    public void testHex_WhenCodepointIsLowercaseLetter_ReturnUppercaseHex() {
        int codepoint = 97;
        String expectedHex = "61";

        String actualHex = CharSequenceTranslator.hex(codepoint);

        assertEquals(expectedHex, actualHex);
    }

    @Test
    public void testHex_WhenCodepointIsUppercaseLetter_ReturnUppercaseHex() {
        int codepoint = 65;
        String expectedHex = "41";

        String actualHex = CharSequenceTranslator.hex(codepoint);

        assertEquals(expectedHex, actualHex);
    }

    // Add more test cases to comprehensively test the hex method

}