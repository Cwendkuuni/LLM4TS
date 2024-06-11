
package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharSequenceTranslatorTest {
    private final CharSequenceTranslator translator = new CharSequenceTranslator() {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }
    };

    @Test
    public void testTranslate() {
        assertNull(translator.translate(null));
        assertEquals("", translator.translate(""));
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());

        writer = new StringWriter();
        translator.translate("", writer);
        assertEquals("", writer.toString());

        writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testCharSequenceTranslatorWithNullWriter() {
        try {
            translator.translate(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCharSequenceTranslatorWithNullInput() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("null");
                return 0;
            }
        };
        translator.translate(null, writer);
        assertEquals("null", writer.toString());
    }

    @Test
    public void testCharSequenceTranslatorWithEmptyInput() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("empty");
                return 0;
            }
        };
        translator.translate("", writer);
        assertEquals("empty", writer.toString());
    }

    @Test
    public void testCharSequenceTranslatorWithNonEmptyInput() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("translated");
                return 0;
            }
        };
        translator.translate("abc", writer);
        assertEquals("translated", writer.toString());
    }

    @Test
    public void testTranslateWithCodePointsConsumed() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("translated");
                return 2; // Simulating consuming 2 codepoints
            }
        };
        translator.translate("abc", writer);
        assertEquals("translated", writer.toString());
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        CharSequenceTranslator result = translator1.with(translator2);

        assertEquals(translator1, result.getTranslators()[0]);
        assertEquals(translator2, result.getTranslators()[1]);
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex('A'));
        assertEquals("7A", CharSequenceTranslator.hex('z'));
        assertEquals("5C", CharSequenceTranslator.hex('\\'));
    }
}
