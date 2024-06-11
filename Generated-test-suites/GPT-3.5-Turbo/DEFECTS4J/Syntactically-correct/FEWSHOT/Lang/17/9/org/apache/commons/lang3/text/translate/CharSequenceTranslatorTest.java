package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.cli.Util;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_nullInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertEquals(null, translator.translate(null));
    }

    @Test
    public void testTranslate_emptyInput() {
        CharSequenceTranslator translator = new DummyTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslate_withTranslator() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequenceTranslator mockTranslator = new DummyTranslator();
        translator.with(mockTranslator);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_withNullWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        try {
            translator.translate("abc", null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withNullInputAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        try {
            translator.translate(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("The Writer must not be null", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslate_withEmptyInputAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate("", writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withTranslatorAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequenceTranslator mockTranslator = new DummyTranslator();
        translator.with(mockTranslator);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_withNullInputAndTranslatorAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
    }

    @Test
    public void testHex_withLowerCase() {
        assertEquals("61", CharSequenceTranslator.hex(97));
    }

    @Test
    public void testHex_withZeroValue() {
        assertEquals("0", CharSequenceTranslator.hex(0));
    }

    @Test
    public void testTranslate_withInputIndexAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_withNullInputIndexAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInputIndexAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate("", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withTranslatorAndInputIndexAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        CharSequenceTranslator mockTranslator = new DummyTranslator();
        translator.with(mockTranslator);
        StringWriter writer = new StringWriter();
        try {
            translator.translate("abc", 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("abc", writer.toString());
    }

    @Test
    public void testTranslate_withNullInputIndexAndTranslatorAndWriter() {
        CharSequenceTranslator translator = new DummyTranslator();
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", writer.toString());
    }

    private static class DummyTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input != null) {
                out.write(input.toString());
            }
            return input.length();
        }

    }
}