import org.junit.*;
import java.text.*;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat_withProperFraction_shouldFormatCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        String result = format.format(new Fraction(3, 4));

        // Assert
        assertEquals("3 / 4", result);
    }

    @Test
    public void testFormat_withWholeNumber_shouldFormatCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        String result = format.format(new Fraction(2, 1));

        // Assert
        assertEquals("2", result);
    }

    @Test
    public void testFormat_withNegativeFraction_shouldFormatCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        String result = format.format(new Fraction(-1, 2));

        // Assert
        assertEquals("-1 / 2", result);
    }

    @Test
    public void testFormat_withNegativeWholeNumber_shouldFormatCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        String result = format.format(new Fraction(-3, 1));

        // Assert
        assertEquals("-3", result);
    }

    @Test
    public void testParse_withValidFractionString_shouldParseCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("3 / 4");

        // Assert
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testParse_withValidWholeNumberString_shouldParseCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("2");

        // Assert
        assertEquals(new Fraction(2, 1), result);
    }

    @Test
    public void testParse_withValidNegativeFractionString_shouldParseCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("-1 / 2");

        // Assert
        assertEquals(new Fraction(-1, 2), result);
    }

    @Test
    public void testParse_withValidNegativeWholeNumberString_shouldParseCorrectly() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("-3");

        // Assert
        assertEquals(new Fraction(-3, 1), result);
    }

    @Test
    public void testParse_withInvalidFractionString_shouldReturnNull() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("3 / 0");

        // Assert
        assertNull(result);
    }

    @Test
    public void testParse_withInvalidWholeNumberString_shouldReturnNull() {
        // Arrange
        ProperFractionFormat format = new ProperFractionFormat();

        // Act
        Fraction result = format.parse("a");

        // Assert
        assertNull(result);
    }
}