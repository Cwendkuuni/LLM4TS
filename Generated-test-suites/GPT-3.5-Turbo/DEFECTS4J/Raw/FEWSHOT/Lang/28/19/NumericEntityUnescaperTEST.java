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
    private Writer out;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
        out = mock(Writer.class);
    }

    @Test
    public void testTranslateWithDecimalEntity() throws IOException {
        CharSequence input = "&#65;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        verify(out).write(65);
        assertEquals(5, result);
    }

    @Test
    public void testTranslateWithHexEntity() throws IOException {
        CharSequence input = "&#x41;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        verify(out).write(65);
        assertEquals(6, result);
    }

    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        CharSequence input = "&#xG;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(0, result);
    }

    @Test
    public void testTranslateWithIncompleteEntity() throws IOException {
        CharSequence input = "&#x41";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(0, result);
    }

    @Test
    public void testTranslateWithInvalidStartingCharacter() throws IOException {
        CharSequence input = "&65;";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(0, result);
    }

    @Test
    public void testTranslateWithInvalidEndingCharacter() throws IOException {
        CharSequence input = "&#x41";
        int index = 0;

        int result = unescaper.translate(input, index, out);

        assertEquals(0, result);
    }
}