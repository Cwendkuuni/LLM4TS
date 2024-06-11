import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_WithValidHexNumericEntity_ShouldAddDecodedCharacterToOutput() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#x41;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WithValidDecimalNumericEntity_ShouldAddDecodedCharacterToOutput() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_WithInvalidNumericEntity_WithoutSemiColon_ShouldReturnZero() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithInvalidNumericEntity_ErrorIfNoSemiColon_ShouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        StringWriter writer = new StringWriter();

        // Act
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testTranslate_WithInvalidHexNumericEntity_ShouldReturnZero() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#x4G;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WithNumericEntityGreaterThan65535_ShouldAddDecodedCharacterToOutput() throws IOException {
        // Arrange
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65536;";
        StringWriter writer = new StringWriter();

        // Act
        int result = unescaper.translate(input, 0, writer);

        // Assert
        assertEquals(9, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

}