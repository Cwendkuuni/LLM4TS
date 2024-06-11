import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;

    @BeforeEach
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withValidNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        // Arrange
        String input = "&#65;";
        StringWriter output = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, output);

        // Assert
        Assertions.assertEquals(5, result);
        Assertions.assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldReturnZero() throws IOException {
        // Arrange
        String input = "&#123z;";
        StringWriter output = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, output);

        // Assert
        Assertions.assertEquals(0, result);
        Assertions.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withHexadecimalNumericEntity_shouldReturnCorrectTranslation() throws IOException {
        // Arrange
        String input = "&#x61;";
        StringWriter output = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, output);

        // Assert
        Assertions.assertEquals(6, result);
        Assertions.assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_withInvalidHexadecimalNumericEntity_shouldReturnZero() throws IOException {
        // Arrange
        String input = "&#xz;";
        StringWriter output = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, output);

        // Assert
        Assertions.assertEquals(0, result);
        Assertions.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withInvalidNumericEntityFormat_shouldReturnZero() throws IOException {
        // Arrange
        String input = "&#";
        StringWriter output = new StringWriter();

        // Act
        int result = numericEntityUnescaper.translate(input, 0, output);

        // Assert
        Assertions.assertEquals(0, result);
        Assertions.assertEquals("", output.toString());
    }
}