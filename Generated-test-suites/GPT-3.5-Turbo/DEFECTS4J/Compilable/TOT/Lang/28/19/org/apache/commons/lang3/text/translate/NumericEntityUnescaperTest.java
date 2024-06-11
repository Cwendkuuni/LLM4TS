package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_ValidDecimalEntity_ShouldReturnCorrectTranslation() throws IOException {
        // Arrange
        CharSequence input = "&#65;";
        Writer writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_ValidHexadecimalEntity_ShouldReturnCorrectTranslation() throws IOException {
        // Arrange
        CharSequence input = "&#x41;";
        Writer writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_InvalidEntity_ShouldReturnZero() throws IOException {
        // Arrange
        CharSequence input = "&#InvalidEntity;";
        Writer writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_PartialEntity_ShouldReturnZero() throws IOException {
        // Arrange
        CharSequence input = "&amp";
        Writer writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_EntireStringNotStartingWithAmpersand_ShouldReturnZero() throws IOException {
        // Arrange
        CharSequence input = "Hello World!";
        Writer writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}