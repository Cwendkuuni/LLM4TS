import org.junit.Before;
import org.junit.Test;

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
    public void testTranslate_withValidDecimalEntity_returnsExpectedOutput() throws IOException {
        // Arrange
        String input = "&#65;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(4, endIndex);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withValidHexadecimalEntity_returnsExpectedOutput() throws IOException {
        // Arrange
        String input = "&#x41;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(6, endIndex);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withMissingSemicolon_returnsZero() throws IOException {
        // Arrange
        String input = "&#65";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, endIndex);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNonNumericEntity_returnsZero() throws IOException {
        // Arrange
        String input = "&#ABC;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, endIndex);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidDecimalValue_returnsZero() throws IOException {
        // Arrange
        String input = "&#99999;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, endIndex);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInvalidHexadecimalValue_returnsZero() throws IOException {
        // Arrange
        String input = "&#xZZ;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(0, endIndex);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withValidDecimalValueGreaterThanFFFF_returnsExpectedOutput() throws IOException {
        // Arrange
        String input = "&#65536;";

        // Act
        StringWriter writer = new StringWriter();
        int endIndex = numericEntityUnescaper.translate(input, 0, writer);

        // Assert
        assertEquals(9, endIndex);
        assertEquals(Character.toString(Character.toChars(65536)[0]) + Character.toString(Character.toChars(65536)[1]), writer.toString());
    }

}