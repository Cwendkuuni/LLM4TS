
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_shouldTranslateNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#65;";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_shouldTranslateHexNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#x41;";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_shouldNotTranslateNonNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&amp;";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }

    @Test
    public void testTranslate_shouldNotTranslateIncompleteNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }

    @Test
    public void testTranslate_shouldNotTranslateInvalidHexNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#xg;";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }

    @Test
    public void testTranslate_shouldNotTranslateInvalidNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#invalid;";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }

    @Test
    public void testTranslate_shouldNotTranslateIncompleteHexNumericEntity() throws IOException {
        // Arrange
        CharSequence input = "&#x";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }

    @Test
    public void testTranslate_shouldNotTranslateEmptyInput() throws IOException {
        // Arrange
        CharSequence input = "";

        // Act
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertTrue(writer.toString().isEmpty());
    }
}
