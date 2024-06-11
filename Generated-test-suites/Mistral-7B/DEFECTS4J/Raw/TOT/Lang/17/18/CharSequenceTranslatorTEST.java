package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method of default CharSequenceTranslator.
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        String text = "Hello World! こんにちは世界！";
        String translatedText = translator.translate(text);
        String expectedText = new String(StringUtils.toByteArray(text), 0, text.length());
        String expectedTranslatedText = new String(StringUtils.toByteArray(expectedText), 0, expectedText.length());

        assertEquals("Expected same text before and after translation", expectedTranslatedText, translatedText);
    }

    /**
     * Test for translate method of AggregateTranslator with default CharSequenceTranslator and hex translator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator defaultTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        CharSequenceTranslator hexTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (Character.isDigit(Character.codePointAt(input, index))) {
                    out.write(Character.codePointAt(input, index));
                    out.write(' ');
                    out.write(hex(Character.codePointAt(input, index)).charAt(0));
                    out.write(hex(Character.codePointAt(input, index)).charAt(1));
                    return 2;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{defaultTranslator, hexTranslator});

        String text = "Hello World! こんにちは世界！";
        String translatedText = translator.translate(text);
        String expectedText = new String(StringUtils.toByteArray(text), 0, text.length());
        String expectedTranslatedText = "48656C6C6F20576F726C6421 56656C6C6F6E67657220756E6374696F6E21 7683516765207365727369616C6C617374206578636520656E636F6465207765726B69646564";

        assertEquals("Expected same text before and after translation", expectedTranslatedText, translatedText);
    }
}