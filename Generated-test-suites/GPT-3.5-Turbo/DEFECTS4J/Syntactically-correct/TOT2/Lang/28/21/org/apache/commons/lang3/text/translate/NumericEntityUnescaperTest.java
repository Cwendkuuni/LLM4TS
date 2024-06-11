import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_validDecimalEntity() {
        // Given
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#65;"; // Decimal entity for 'A'

        // When
        int index = unescaper.translate(input, 0, output);

        // Then
        assertEquals(4, index); // Expected output length
        assertEquals("A", output.toString()); // Expected output
    }

    @Test
    public void testTranslate_validHexEntity() {
        // Given
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#x41;"; // Hex entity for 'A'

        // When
        int index = unescaper.translate(input, 0, output);

        // Then
        assertEquals(5, index); // Expected output length
        assertEquals("A", output.toString()); // Expected output
    }

    @Test
    public void testTranslate_invalidEntity() {
        // Given
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#invalid;"; // Invalid entity

        // When
        int index = unescaper.translate(input, 0, output);

        // Then
        assertEquals(0, index); // Expected no translation
        assertEquals("", output.toString()); // Expected empty output
    }

    @Test
    public void testTranslate_outOfBounds() {
        // Given
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        CharSequence input = "&#65;"; // Decimal entity for 'A'

        // When
        int index = unescaper.translate(input, 1, output); // Index out of bounds

        // Then
        assertEquals(0, index); // Expected no translation
        assertEquals("", output.toString()); // Expected empty output
    }
}