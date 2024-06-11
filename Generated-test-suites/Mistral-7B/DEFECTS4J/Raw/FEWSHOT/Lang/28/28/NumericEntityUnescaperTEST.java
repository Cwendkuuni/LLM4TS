package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSingleCharacterEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "&amp;";
        int index = 0;

        int length = unescaper.translate(input, index, out);
        assertEquals("&", input.subSequence(0, index).toString());
        assertEquals("amp;", input.subSequence(index, input.length()).toString());
        assertEquals("&", outputStream.toString().charAt(0), 0);
        assertEquals('a', outputStream.toString().charAt(1), 0);
        assertEquals('m', outputStream.toString().charAt(2), 0);
        assertEquals('p', outputStream.toString().charAt(3), 0);
        verify(out).flush();
        verify(out).close();
        assertEquals(4, length);
    }

    @Test
    public void testTranslateHexCharacterEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "&#x41;";
        int index = 0;

        int length = unescaper.translate(input, index, out);
        assertEquals("", input.subSequence(0, index).toString());
        assertEquals("#x41;", input.subSequence(index, input.length()).toString());
        assertEquals('A', outputStream.toString().charAt(0), 0);
        verify(out).flush();
        verify(out).close();
        equals(1, length);
    }

    @Test
    public void testTranslateDecimalCharacterEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "&#101;";
        int index = 0;

        int length = unescaper.translate(input, index, out);
        assertEquals("", input.subSequence(0, index).toString());
        assertEquals("#101;", input.subSequence(index, input.length()).toString());
        assertEquals('e', outputStream.toString().charAt(0), 0);
        verify(out).flush();
        verify(out).close();
        equals(1, length);
    }

    @Test
    public void testTranslateEntityWithInvalidSyntax() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "&xx";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, index, out);
        });

        assertEquals("&xx", input.subSequence(index, input.length()).toString());
        verify(out, never()).write(anyInt());
        verify(out).flush();
        verify(out).close();
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAfterHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "&%x41;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, index, out);
        });

        assertEquals("&%x41;", input.subSequence(index, input.length()).toString());
        verify(out, never()).write(anyInt());
        verify(out).flush();
        verify(out).close();
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterBeforeHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out = new Writer() {
            @Override
            public void write(int c, java.io.Writer writer) throws IOException {
                writer.write(c);
            }

            @Override
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override
            public void close() throws IOException {
                outputStream.close();
            }
        };

        CharSequence input = "%&x41;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, index, out);
        });

        assertEquals("%&x41;", input.subSequence(index, input.length()).toString());
        verify(out, never()).write(anyInt());
        verify(out).flush();
        verify(out).close();
    }
}