package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.EmptyTranslator;
import org.apache.commons.lang3.text.translate.StringEscapeUtils;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with empty translator.
     */
    @Test
    public void testTranslateEmptyTranslator() {
        CharSequenceTranslator translator = new EmptyTranslator();
        CharSequence input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with default translator.
     */
    @Test
    public void testTranslateDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };
        CharSequence input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with StringEscapeUtils translator.
     */
    @Test
    public void testTranslateStringEscapeUtilsTranslator() {
        CharSequenceTranslator translator = new StringEscapeUtils();
        CharSequence input = "Hello, <world>!";
        String expectedOutput = "Hello, \\< world \\>!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test translate method with custom translator.
     */
    @Test
    public void testTranslateCustomTranslator() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (Character.isDigit(Character.codePointAt(input, index))) {
                    out.write('1');
                    return 1;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (Character.isUpperCase(Character.codePointAt(input, index))) {
                    out.write('A');
                    return 1;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };

        CharSequenceTranslator translator = translator1.with(translator2);
        CharSequence input = "1A1B2C3D";
        String expectedOutput = "1A1A1B1B2C2C3D3D";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }
}