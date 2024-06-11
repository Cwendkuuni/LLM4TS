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
    public void testTranslate_ValidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[xAB]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) out;
        assertEquals((byte) 0xAB, outputStream.toByteArray()[0]);
    }

    @Test
    public void testTranslate_ValidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[123]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) out;
        assertEquals((byte) 0x7B, outputStream.toByteArray()[0]);
    }

    @Test
    public void testTranslate_InvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&[#invalid]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithInvalidCharacterAfterHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[a]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithNoNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&[x]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithTrailingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[123];";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(1, result);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) out;
        assertEquals((byte) 0x7B, outputStream.toByteArray()[0]);
    }

    @Test
    public void testTranslate_EntityWithLeadingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = ";&#[123]";
        Writer out = mock(Writer.class);

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, out));
    }

    @Test
    public void testTranslate_EntityWithNoHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&[123]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithNoNumberAfterHash() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[];";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 0, out);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_EntityWithLeadingWhitespace() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = " &#[123]";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, 1, out);
        assertEquals(1, result);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) out;
        assertEquals((byte) 0x7B, outputStream.toByteArray()[0]);
    }

    @Test
    public void testTranslate_EntityWithTrailingWhitespace() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#[123] ";
        Writer out = mock(Writer.class);

        int result = unescaper.translate(input, input.length() - 1, out);
        assertEquals(1, result);

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) out;
        assertEquals((byte) 0x7B, outputStream.toByteArray()[0]);
    }
}