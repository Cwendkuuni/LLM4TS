package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    private Writer out;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        out = mock(Writer.class);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream);
    }

    @Test
    public void testTranslate_ValidNumericEntityWithDecimal_ShouldCorrectlyUnescapeCharacter() {
        // Given
        CharSequence input = "&amp;#128;";
        int index = 0;

        // When
        int result = new NumericEntityUnescaper().translate(input, index, out);

        // Then
        assertEquals(2, result);
        assertEquals("\u0080", outputStream.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithHex_ShouldCorrectlyUnescapeCharacter() {
        // Given
        CharSequence input = "&amp;#x41;";
        int index = 0;

        // When
        int result = new NumericEntityUnescaper().translate(input, index, out);

        // Then
        assertEquals(2, result);
        assertEquals("A", outputStream.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithoutSemicolon_ShouldThrowAnException() {
        // Given
        CharSequence input = "&amp;#123";
        int index = 0;

        // When
        assertThrows(IOException.class, () -> new NumericEntityUnescaper().translate(input, index, out));

        // Then
        verify(out, never()).write(anyChar());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithNegativeValue_ShouldThrowAnException() {
        // Given
        CharSequence input = "&amp;#-1;";
        int index = 0;

        // When
        assertThrows(IOException.class, () -> new NumericEntityUnescaper().translate(input, index, out));

        // Then
        verify(out, never()).write(anyChar());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithValueGreaterThanUShortMax_ShouldCorrectlyUnescapeTwoCharacters() {
        // Given
        CharSequence input = "&amp;#65536;";
        int index = 0;

        // When
        int result = new NumericEntityUnescaper().translate(input, index, out);

        // Then
        assertEquals(4, result);
        assertEquals("\uD800\uDC00", outputStream.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithValueGreaterThanUCharMax_ShouldCorrectlyUnescapeThreeCharacters() {
        // Given
        CharSequence input = "&amp;#1114112;";
        int index = 0;

        // When
        int result = new NumericEntityUnescaper().translate(input, index, out);

        // Then
        assertEquals(5, result);
        assertEquals("\uD835\uDD1E\uD835\uDD1F\uD835\uDD20", outputStream.toString());
    }

    @Test
    public void testTranslate_EmptyInput_ShouldReturnZero() {
        // Given
        CharSequence input = "";
        int index = 0;

        // When
        int result = new NumericEntityUnescaper().translate(input, index, out);

        // Then
        assertEquals(0, result);
        verify(out, never()).write(anyChar());
    }

    @Test
    public void testTranslate_NullInput_ShouldThrowAnException() {
        // Given
        CharSequence input = null;
        int index = 0;

        // When
        assertThrows(NullPointerException.class, () -> new NumericEntityUnescaper().translate(input, index, out));

        // Then
        verify(out, never()).write(anyChar());
    }

    @Test
    public void testTranslate_IndexOutOfBounds_ShouldThrowAnException() {
        // Given
        CharSequence input = "&amp;";
        int index = input.length();

        // When
        assertThrows(IndexOutOfBoundsException.class, () -> new NumericEntityUnescaper().translate(input, index, out));

        // Then
        // The out.write() call should not be executed
        verify(out, never()).write(anyChar());
    }

    private Writer writer;
    private Writer out;
    private ByteArrayOutputStream outputStream;
}