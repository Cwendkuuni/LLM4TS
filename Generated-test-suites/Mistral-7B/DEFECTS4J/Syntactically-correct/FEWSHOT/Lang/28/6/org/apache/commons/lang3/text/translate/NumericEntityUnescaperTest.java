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
    public void testTranslateWithDecimalEntities() throws IOException {
        CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#41; &#123; &#65;&#69;&#6E;&#65;&#74; &#x3A;" +
                " &#10; &#13; &#32; &#171;";
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                outputStream.write(cbuf, off, len);
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

        unescaper.translate(input, 0, writer);
        assertEquals("abcdefghijk&", outputStream.toString());

        verify(out).write(97); // 'a'
        verify(out).write(99); // 'c'
        verify(out).write(104); // 'h'
        verify(out).write(101); // 'e'
        verify(out).write(111); // 'o'
        verify(out).write(116); // 't'
        verify(out).write(115); // 's'
        verify(out).write(58); // ':'
        verify(out).write(50); // '2'
        verify(out).write(51); // '1'
    }

    @Test
    public void testTranslateWithHexEntities() throws IOException {
        CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#x26;&#xA0;&#xA3;&#x23; &#x61;&#x6C;&#x65;&#x62;&#x72;&#x6F;" +
                " &#x304;&#xC0;&#xA2;&#xC1;&#xC4;&#x201E;";
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                outputStream.write(cbuf, off, len);
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

        unescaper.translate(input, 0, writer);
        assertEquals("abcdefghij&amp;#x168;&#x2030;&#x2018;&#x61;&#x6C;&#x65;&#x62;&#x72;&#x6F;&#x304;&#xC1;&#xA2;&#xC4;", outputStream.toString());

        verify(out).write(97); // 'a'
        verify(out).write(115); // 'u'
        verify(out).write(101); // 'e'
        verify(out).write(116); // 't'
        verify(out).write(105); // 'i'
    }

    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        CharSequence input = "&invalidEntity;";
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NumberFormatException.class, () -> unescaper.translate(input, 0, out));
    }

    @Test
    public void testTranslateWithNegativeIndex() {
        CharSequence input = "test";
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(input, -1, out));
    }

    @Test
    public void testTranslateWithNullInput() {
        Writer out = mock(Writer.class);
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, out));
    }

    @Test
    public void testTranslateWithNullOutput() {
        CharSequence input = "test";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> unescaper.translate(input, 0, null));
    }
}