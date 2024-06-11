package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.HexTranslator;

public class CharSequenceTranslatorTest {

    /**
     * Test translate method with no translators.
     */
    @Test
    public void testTranslateNoTranslators() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        String input = "abcdef";
        String expected = "abcdef";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test translate method with HexTranslator translator.
     */
    @Test
    public void testTranslateHexTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) >= '0' && input.charAt(index) <= '9') {
                    out.write(48 + Character.digit(input.charAt(index), 16));
                    return 1;
                } else if (input.charAt(index) >= 'A' && input.charAt(index) <= 'F') {
                    out.write(52 + Character.digit(input.charAt(index), 16));
                    return 1;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        }.with(new HexTranslator());

        String input = "abcdEf";
        String expected = "414244454546";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test translate method with custom translator.
     */
    @Test
    public void testTranslateCustomTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('z');
                    return 1;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };

        String input = "abc";
        String expected = "zbc";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test translate method with null input.
     */
    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input == null) {
                    assertNull(out);
                    return 0;
                }
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        String input = null;
        String expected = null;
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.length() == 0) {
                    return 0;
                }
                out.write(Character.codePointAt(input, 0));
                return Character.charCount(Character.codePointAt(input, 0));
            }
        };

        String input = "";
        String expected = "";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test translate method with writer null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        String input = "abc";
        Writer out = null;
        translator.translate(input, 0, out);
    }

    /**
     * Test translate method with custom translator and writer.
     */
    @Test
    public void testTranslateCustomTranslatorWithWriter() throws IOException {
        StringWriter writer = new StringWriter();

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('z');
                    return 1;
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };

        String input = "abc";
        translator.translate(input, 0, writer);
        String expected = "zbc";
        String output = writer.toString();
        assertEquals(expected, output);
    }

    /**
     * Test translate method with AggregateTranslator.
     */
    @Test
    public void testTranslateAggregateTranslator() {
        CharSequenceTranslator hextoascii = new HexTranslator();
        CharSequenceTranslator asciitohex = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(hex(Character.codePointAt(input, index)).toCharArray()[0]);
                return 1;
            }
        };

        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (Character.isDigit(Character.codePointAt(input, index))) {
                    return hextoascii.translate(input, index, out);
                } else {
                    return asciitohex.translate(input, index, out);
                }
            }
        }.with(hextoascii, asciitohex);

        String input = "12abCDEF";
        String expected = "histoire";
        String output = translator.translate(input);
        assertTrue(expected.equals(output));
    }
}