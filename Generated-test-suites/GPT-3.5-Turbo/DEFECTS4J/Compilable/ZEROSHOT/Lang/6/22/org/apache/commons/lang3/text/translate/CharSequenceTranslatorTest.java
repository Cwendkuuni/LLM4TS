package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringBuilder input = new StringBuilder("input");
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringBuilder input = new StringBuilder("input");
        String output = translator.translate(input);
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringBuilder input = new StringBuilder("input");
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        String output = translator.translate(null);
        assertNull(output);
    }

    @Test
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        StringBuilder input = new StringBuilder("input");
        try {
            translator.translate(input, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            fail("Expected IllegalArgumentException to be thrown");
        }
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

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHex() {
        String hex = CharSequenceTranslator.hex(65);
        assertEquals("41", hex);
    }

}