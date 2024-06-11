package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        String expected = "translated string";
        when(translator.translate(input)).thenReturn(expected);
        String result = translator.translate(input);
        assertEquals(expected, result);
        verify(translator, times(1)).translate(input);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = null;
        String result = translator.translate(input);
        assertNull(result);
        verify(translator, times(0)).translate(input);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        String expected = "translated string";
        Writer writer = new StringWriter();
        when(translator.translate(input, writer)).thenReturn(10);
        translator.translate(input, writer);
        assertEquals(expected, writer.toString());
        verify(translator, times(1)).translate(input, writer);
    }

    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        Writer writer = null;
        translator.translate(input, writer);
        verify(translator, times(0)).translate(input, writer);
    }

    @Test
    public void testTranslateWithCharSequenceAndIndex() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        Writer writer = new StringWriter();
        int index = 0;
        int expected = 5;
        when(translator.translate(input, index, writer)).thenReturn(expected);
        int result = translator.translate(input, index, writer);
        assertEquals(expected, result);
        verify(translator, times(1)).translate(input, index, writer);
    }

    @Test
    public void testTranslateWithNullCharSequenceAndIndex() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = null;
        Writer writer = new StringWriter();
        int index = 0;
        int result = translator.translate(input, index, writer);
        assertEquals(0, result);
        verify(translator, times(0)).translate(input, index, writer);
    }

    @Test
    public void testTranslateWithCharSequenceAndNullWriter() throws IOException {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        Writer writer = null;
        int index = 0;
        translator.translate(input, index, writer);
        verify(translator, times(0)).translate(input, index, writer);
    }

    @Test
    public void testTranslateWithCharSequenceAndWriterThrowsIOException() throws IOException {
        Writer writer = mock(Writer.class);
        doThrow(IOException.class).when(writer).write(any(char[].class), anyInt(), anyInt());
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        String input = "test string";
        int index = 0;
        int result = translator.translate(input, index, writer);
        assertEquals(0, result);
        verify(translator, times(1)).translate(input, index, writer);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator = mock(CharSequenceTranslator.class);
        CharSequenceTranslator[] translators = new CharSequenceTranslator[]{translator};
        CharSequenceTranslator result = translator.with(translators);
        assertEquals(translator.getClass().getName(), result.getClass().getName());
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    @Test
    public void testHexWithLowerCaseLocale() {
        int codepoint = 65;
        String expected = "41";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }
}