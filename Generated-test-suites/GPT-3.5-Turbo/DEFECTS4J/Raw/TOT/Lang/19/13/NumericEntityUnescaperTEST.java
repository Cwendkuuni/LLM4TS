import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper;

    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
    }

    // Test cases for translate method

    @Test
    public void testTranslate_withValidDecimalEntity_returnsLengthOfTranslatedEntity() throws IOException {
        // Arrange
        String input = "&#65;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidHexEntity_returnsLengthOfTranslatedEntity() throws IOException {
        // Arrange
        String input = "&#x20AC;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(8, result);
        assertEquals("€", writer.toString());
    }

    @Test
    public void testTranslate_withMissingSemicolon_returnsZero() throws IOException {
        // Arrange
        String input = "&#65";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidDecimalEntity_returnsZero() throws IOException {
        // Arrange
        String input = "&#99999999;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexEntity_returnsZero() throws IOException {
        // Arrange
        String input = "&#xZZZZ;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_returnsZero() throws IOException {
        // Arrange
        String input = "";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    // Additional test cases for other methods in the class

    // ...

}