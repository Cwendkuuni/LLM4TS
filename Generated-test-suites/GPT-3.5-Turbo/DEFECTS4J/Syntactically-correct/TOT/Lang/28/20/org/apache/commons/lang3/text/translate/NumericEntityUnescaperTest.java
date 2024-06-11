import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_WithValidNumericEntity_ShouldTranslateAndReturnValidCodePoint() throws IOException {
        // Arrange
        String input = "&#65;"; // Numeric entity for letter 'A'
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 4", 4, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }

    @Test
    public void testTranslate_WithValidHexadecimalEntity_ShouldTranslateAndReturnValidCodePoint() throws IOException {
        // Arrange
        String input = "&#x41;"; // Hexadecimal entity for letter 'A'
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 5", 5, result);
        assertEquals("Writer should contain 'A'", "A", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidNumericEntity_ShouldNotTranslateAndReturnZero() throws IOException {
        // Arrange
        String input = "&#XYZ;"; // Invalid numeric entity
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidHexadecimalEntity_ShouldNotTranslateAndReturnZero() throws IOException {
        // Arrange
        String input = "&#xG;"; // Invalid hexadecimal entity
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithIncompleteEntity_ShouldNotTranslateAndReturnZero() throws IOException {
        // Arrange
        String input = "&#65"; // Incomplete numeric entity
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidStartingCharacter_ShouldNotTranslateAndReturnZero() throws IOException {
        // Arrange
        String input = "$#65;"; // Invalid starting character
        StringWriter writer = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals("Result should be 0", 0, result);
        assertEquals("Writer should be empty", "", writer.toString());
    }
}