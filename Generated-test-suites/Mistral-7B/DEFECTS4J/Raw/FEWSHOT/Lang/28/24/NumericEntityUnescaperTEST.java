package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#32; &#123; &#x20ac; &#x1F600; &#xFFFD;";
        int index = 0;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer output = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }
        };

        for (int i = 0; i < input.length(); i++) {
            int len = unescaper.translate(input, index, output);
            index += len;
        }

        String expected = "&amp; &lt; &gt; &#32; &#123; &#x20ac; &#x1F600; &#xFFFD;";
        String actual = outputStream.toString();
        assertEquals(expected, actual);

        verify(out).write(65279); // UTF-8 codepoint for '⃝' (#xFFFD)
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&invalidEntity;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidEntityStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&invalidEntityStart";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidEntityEnd() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&invalidEntity;&";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidHexEntityStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&x#";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidHexEntityLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&x#abc";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithInvalidHexEntityEnd() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&x#abc;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithHexEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&x20ac;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(1, len);
        assertEquals(8364, out.getBuffer().read()); // UTF-8 codepoint for '€' (#x20ac)
    }

    @Test
    public void testTranslateWithDecEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#123;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(1, len);
        assertEquals(123, out.getBuffer().read());
    }

    @Test
    public void testTranslateWithDecEntityStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithDecEntityEnd() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithDecEntityLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#123AB;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(1, len);
        assertEquals(123, out.getBuffer().read());
    }

    @Test
    public void testTranslateWithDecEntityOverflow() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#2147483647;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithDecEntityInvalid() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&#-1;";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(0, len);
    }

    @Test
    public void testTranslateWithLeadingWhitespace() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = " &amp; ";
        int index = 0;

        int len = unescaper.translate(input, index, out);
        assertEquals(1, len);
        assertEquals(65274, out.getBuffer().read()); // UTF-8 codepoint for '&'
    }

    @Test
    public void testTranslateWithTrailingWhitespace() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = mock(Writer.class);
        CharSequence input = "&amp; ";
        int index = input.length();

        int len = unescaper.translate(input, index, out);
        assertEquals(1, len);
        assertEquals(65274, out.getBuffer().read()); // UTF-8 codepoint for '&'
    }
}