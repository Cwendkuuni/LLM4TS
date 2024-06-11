package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        StringWriter writer = new StringWriter();

        assertNull(translator.translate(null, writer));
        verify(translator, never()).translate(any(CharSequence.class), anyInt(), any(Writer.class));
    }

    @Test
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "test";

        assertNull(translator.translate(input, null));
        verify(translator, times(1)).translate(input, any(Writer.class));
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);
        assertEquals("test", writer.toString());
        assertEquals(input.length(), consumed);
        verify(translator, times(1)).translate(input, writer);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        assertEquals("test", writer.toString());
    }

    @Test
    public void testTranslateWithWriterThrowsIOException() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        StringWriter writer = new StringWriter();

        doThrow(new IOException("Writer error")).when(translator).translate(any(CharSequence.class), anyInt(), any(Writer.class));
        String input = "test";

        try {
            translator.translate(input, writer);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Writer error", e.getMessage());
        }

        verify(translator, times(1)).translate(input, writer);
    }

    @Test
    public void testTranslateWithOutputStream() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        translator.translate(input, outputStream);
        assertEquals("test", new String(outputStream.toByteArray()));
    }

    @Test
    public void testTranslateWithInputStream() throws IOException {
        CharSequence input = "test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StringWriter writer = new StringWriter(outputStream);

        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, writer);

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        CharSequence output = translator.translate(input, inputStream);

        assertEquals("test", output);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new UpperCaseTranslator();

        CharSequenceTranslator composite = translator1.with(translator2);
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        composite.translate(input, writer);
        assertEquals("TEST", writer.toString());
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("0410", CharSequenceTranslator.hex(0x410));
        assertEquals("26", CharSequenceTranslator.hex(0x26));
        assertEquals("4241", CharSequenceTranslator.hex(0x4241));
        assertEquals("00004241", CharSequenceTranslator.hex(0x00004241));
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index < 0 || index >= input.length()) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index < 0 || index >= input.length()) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        out.write(Character.toUpperCase(c));
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;

        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            if (consumed == input.length()) {
                break;
            }
            index += consumed;
        }

        return consumed;
    }
}