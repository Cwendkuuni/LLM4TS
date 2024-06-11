package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateNormalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };
        when(writer.write(anyInt())).thenDelegation(out);

        CharSequence input = "abc&amp;#123;&#x1F0A;def";
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            int result = unescaper.translate(input, index, writer);
            index += result;
            if (result > 0) {
                assertEquals(input.charAt(index - 1), input.charAt(i));
            }
        }

        byte[] expectedOutput = new byte[]{'a', 'b', 'c', 123, 37315, 'd', 'e', 'f'};
        assertArrayEquals(expectedOutput, outStream.toByteArray());
    }

    @Test
    public void testTranslateHexCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };
        when(writer.write(anyInt())).thenDelegation(out);

        CharSequence input = "abc&amp;#x1F0A;def";
        int index = 0;
        int length = input.length();

        for (int i = 0; i < length; i++) {
            int result = unescaper.translate(input, index, writer);
            index += result;
            if (result > 0) {
                assertEquals(input.charAt(index - 1), input.charAt(i));
            }
        }

        byte[] expectedOutput = new byte[]{'a', 'b', 'c', (byte) 37315, 'd', 'e', 'f'};
        assertArrayEquals(expectedOutput, outStream.toByteArray());
    }

    @Test
    public void testTranslateEmptySequence() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };
        when(writer.write(anyInt())).thenDelegation(out);

        CharSequence input = "";
        int index = 0;
        int length = input.length();

        int result = unescaper.translate(input, index, writer);
        index += result;
        assertNull(result);
        assertEquals("", input.subSequence(0, index));
    }

    @Test
    public void testTranslateInvalidSequence() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = mock(Writer.class);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Writer out = new Writer() {
            @Override
    public void write(int c) throws IOException {
                outStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };
        when(writer.write(anyInt())).thenDelegation(out);

        CharSequence input = "abc&amp;#xinvalid#def";
        int index = 0;
        int length = input.length();

        int result = unescaper.translate(input, index, writer);
        index += result;
        assertEquals(0, result);
        assertEquals("abc&amp;#xinvalid#def", input.subSequence(0, index));
    }
}