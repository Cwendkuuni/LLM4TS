package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_DecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "This is a &123; test.";
        int index = 5;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        verify(out).write(123);
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testTranslate_HexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "This is a &#x1F44D; test.";
        int index = 5;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        verify(out).write(35161);
        assertEquals("\uD83D\uDC4D", outputStream.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "This is a &123X; test.";
        int index = 5;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        verify(out, mockito.never()).write(123);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testTranslate_InvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "This is a &#x1F44X; test.";
        int index = 5;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        verify(out, mockito.never()).write(35161);
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testTranslate_NullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = null;

        Writer out = mock(Writer.class);
        int index = 0;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        assertNull(outputStream.toString());
    }

    @Test
    public void testTranslate_NullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&123;";
        int index = 0;

        Writer out = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, out);
        assertNull(outputStream
                .toString());
    }

    @Test
    public void testTranslate_EmptyEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "This is a &nbsp; test.";
        int index = 4;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c, java.io.IOException e) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {}
        };

        unescaper.translate(input, index, writer);
        verify(out).write(32);
        assertEquals(" ", outputStream.toString());
    }
}