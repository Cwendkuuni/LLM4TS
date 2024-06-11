package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    // Test translate method with basic functionality
    @Test
    public void testTranslate_BasicFunctionality() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index) + " translated");
                return 1;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("input", writer);

        assertEquals("i translated", writer.toString());
    }

    // Test translate method with null input
    @Test
    public void testTranslate_NullInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index) + " translated");
                return 1;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate(null, writer);

        assertEquals("", writer.toString());
    }

    // Test translate method with null writer
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index) + " translated");
                return 1;
            }
        };

        translator.translate("input", null);
    }

    // Test translate method with no consumed codepoints
    @Test
    public void testTranslate_NoConsumedCodepoints() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringWriter writer = new StringWriter();
        translator.translate("input", writer);

        assertEquals("input", writer.toString());
    }

    // Test with the hex method for a positive number
    @Test
    public void testHex_PositiveNumber() {
        String hexString = CharSequenceTranslator.hex(10);

        assertEquals("A", hexString);
    }

    // Test with the hex method for a negative number
    @Test
    public void testHex_NegativeNumber() {
        String hexString = CharSequenceTranslator.hex(-10);

        assertEquals("FFFFFFF6", hexString);
    }
}

