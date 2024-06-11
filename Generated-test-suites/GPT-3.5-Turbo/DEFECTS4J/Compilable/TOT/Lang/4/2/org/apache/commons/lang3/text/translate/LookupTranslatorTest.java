package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setUp() {
        // Test lookup table
        CharSequence[][] lookup = { 
            { "1", "One" }, 
            { "2", "Two" }, 
            { "3", "Three" } 
        };
        translator = new LookupTranslator(lookup);
    }

    @Test
    public void testTranslate_withExistingKey_ShouldReturnMatchedValue() throws IOException {
        // Arrange
        String input = "2";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 1;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("Two", writer.toString());
    }

    @Test
    public void testTranslate_withNonExistingKey_ShouldReturnZero() throws IOException {
        // Arrange
        String input = "4";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 0;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withPartialMatch_ShouldReturnMatchedValue() throws IOException {
        // Arrange
        String input = "23";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 2;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("Two3", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_ShouldReturnZero() throws IOException {
        // Arrange
        String input = "";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 0;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNullInput_ShouldReturnZero() throws IOException {
        // Arrange
        String input = null;
        StringWriter writer = new StringWriter();

        // Act
        int expected = 0;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withIndexGreaterThanInputLength_ShouldReturnZero() throws IOException {
        // Arrange
        String input = "123";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 0;
        int actual = translator.translate(input, 4, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withMultipleMatches_ShouldReturnLongestMatchedValue() throws IOException {
        // Arrange
        CharSequence[][] lookup = {
            { "cat", "Tiger" },
            { "catch", "Capture" }
        };
        translator = new LookupTranslator(lookup);
        String input = "catch";
        StringWriter writer = new StringWriter();

        // Act
        int expected = 5;
        int actual = translator.translate(input, 0, writer);

        // Assert
        assertEquals(expected, actual);
        assertEquals("Capture", writer.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_withIOExceptionThrown_ShouldThrowIOException() throws IOException {
        // Arrange
        CharSequence[][] lookup = { 
            { "a", "A" } 
        };
        translator = new LookupTranslator(lookup);
        String input = "a";
        StringWriter writer = new StringWriter();
        writer.close(); // Close the writer to cause IOException

        // Act
        translator.translate(input, 0, writer);
    }

}