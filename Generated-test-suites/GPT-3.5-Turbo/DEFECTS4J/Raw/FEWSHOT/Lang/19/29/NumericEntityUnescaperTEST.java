package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidDecimalEntity() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "&#123;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(6, result);
        verify(out, times(1)).write(123);
    }

    @Test
    public void testTranslate_ValidHexEntity() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "&#xABC;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(8, result);
        verify(out, times(1)).write(0xABC);
    }

    @Test
    public void testTranslate_MissingSemicolon() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "&#123";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(3, result);
        verify(out, never()).write(anyInt());
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_InvalidNumberFormat() throws IOException {
        Writer out = mock(Writer.class);
        CharSequence input = "&#ABC;";
        int index = 0;

        unescaper.translate(input, index, out);
    }

    @Test(expected = IOException.class)
    public void testTranslate_IoException() throws IOException {
        Writer out = mock(Writer.class);
        doThrow(IOException.class).when(out).write(anyInt());
        CharSequence input = "&#123;";
        int index = 0;

        unescaper.translate(input, index, out);
    }
}