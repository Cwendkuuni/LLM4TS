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
    public void testTranslate_ValidEntities_Hex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&#x41;&#x42;";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertEquals(4, length);
        assertEquals("AB", outputStream.toString());

        verify(writer).write((char) 0x41);
        verify(writer).write((char) 0x42);
    }

    @Test
    public void testTranslate_ValidEntities_Decimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&#10;";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertEquals(2, length);
        assertEquals("\n", outputStream.toString());

        verify(writer).write('\n');
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&abc;";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertEquals(0, length);

        // The test should not write anything to the writer
        verify(writer, never()).write(anyChar());
    }

    @Test
    public void testTranslate_EntityOutsideRange() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&fffe;";
        int index = 0;

        assertThrows(IOException.class, () -> {
            int length = unescaper.translate(new CharSequence() {
                @Override
                public int length() {
                    return input.length();
                }

                @Override
                public char charAt(int index) {
                    return input.charAt(index);
                }
            }, index, writer);
        });

        // The test should not write anything to the writer
        verify(writer, never()).write(anyChar());
    }

    @Test
    public void testTranslate_EntityWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&#x41;";
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertEquals(2, length);
        assertEquals("A", outputStream.toString());

        verify(writer).write((char) 0x41);
    }

    @Test
    public void testTranslate_EntityWithoutSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer out = outputStream::write;

        String input = "&amp;"; // entity for '&'
        int index = 0;

        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);

        assertEquals(1, length);
        assertEquals("&", outputStream.toString());

        verify(writer).write('&');
    }
}