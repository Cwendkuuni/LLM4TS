
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() {
        // Create a mock translator to test the translate() method
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Mock translation that just writes the input to the output
                out.write(input.toString());
                return input.length();
            }
        };

        // Test translation with a non-null input
        String input = "Hello";
        assertEquals(input, translator.translate(input));

        // Test translation with a null input
        assertNull(translator.translate(null));
    }

    @Test
    public void testWith() {
        // Create two mock translators to test the with() method
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Mock translation that just writes the input to the output
                out.write(input.toString());
                return input.length();
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                // Mock translation that just appends " World" to the input
                out.write(input.toString() + " World");
                return input.length() + 6;
            }
        };

        // Test with() method with two translators
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String input = "Hello";
        StringWriter writer = new StringWriter();
        try {
            combinedTranslator.translate(input, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Hello World", writer.toString());
    }

    @Test
    public void testHex() {
        // Test hex() method with a valid input
        int codepoint = 65; // 'A' in ASCII
        assertEquals("41", CharSequenceTranslator.hex(codepoint));

        // Test hex() method with an invalid input
        codepoint = -1;
        assertEquals("FFFFFFFF", CharSequenceTranslator.hex(codepoint));
    }
}
