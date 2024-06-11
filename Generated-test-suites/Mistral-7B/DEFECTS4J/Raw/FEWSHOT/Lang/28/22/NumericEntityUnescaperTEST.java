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
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "";

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateNoEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "abc";

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateSimpleEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&amp;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('&');
    }

    @Test
    public void testTranslateSimpleEntityWithLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&amp;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('&');
    }

    @Test
    public void testTranslateEntityWithHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#x41;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('A');
    }

    @Test
    public void testTranslateEntityWithHexAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#x41;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('A');
    }

    @Test
    public void testTranslateEntityWithDecimal() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#101;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('e');
    }

    @Test
    public void testTranslateEntityWithDecimalAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "#101;";

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);
        verify(out).write('e');
    }

    @Test
    public void testTranslateEntityWithInvalidFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#x";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidFormatAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "#x";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAfterHexPrefix() throws IOException {
        NumericEntityUnescaper unescaper = new NunicodeEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#xAz";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAfterDecimalPrefix() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#123a";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterInNumericPart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#[abc]123;";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterInNumericPartAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "#[abc]123;";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAfterNumericPart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&#[123];a";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAfterNumericPartAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "#[123];a";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAtStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&%#123;";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterAtStartAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "%#123;";

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterInEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "&&#123;";

        assertThrows(IOException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateEntityWithInvalidCharacterInEntityAndLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        CharSequence input = "#&123;";

        assertThrows(IOException.class, () -> {
            unescaper.translate(input, 0, out);
        });

        verify(out, mock().never()).write(anyInt());
    }

    @Test
    public void testTranslateMultipleEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // not used
            }

            @Override
            public void close() throws IOException {
                // not used
            }
        };
        unescaper.setWriter(writer);
        CharSequence input = "abc&amp;#123;&#x41;def";

        int result = unescaper.translate(input, 0, out);
        assertEquals(7, result);
        verify(writer).write('a');
        verify(writer).write('b');
        verify(writer).write('c');
        verify(writer).write('&');
        verify(writer).write('A');
        verify(writer).write('d');
        verify(writer).write('e');
        verify(writer).write('f');
    }

    @Test
    public void testTranslateMultipleEntitiesWithLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // not used
            }

            @Override
            public void close() throws IOException {
                // not used
            }
        };
        unescaper.setWriter(writer);
        CharSequence input = "abc&amp;#123;&#x41;def";

        int result = unescaper.translate(input, 0, out);
        assertEquals(7, result);
        verify(writer).write('a');
        verify(writer).write('b');
        verify(writer).write('c');
        verify(writer).write('&');
        verify(writer).write('A');
        verify(writer).write('d');
        verify(writer).write('e');
        verify(writer).write('f');
    }
}