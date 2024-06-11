import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_BasicDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int index = unescaper.translate("&#65;", 0, result);
        assertEquals(6, index); // Expected index after translation
        assertEquals("A", result.toString()); // Expected result
    }

    @Test
    public void testTranslate_BasicHexadecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int index = unescaper.translate("&#x41;", 0, result);
        assertEquals(7, index); // Expected index after translation
        assertEquals("A", result.toString()); // Expected result
    }

    @Test
    public void testTranslate_ExceptionHandling() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int index = unescaper.translate("&#999999999999999;", 0, result); // Invalid number format
        assertEquals(0, index); // Expect no translation
        assertEquals("", result.toString()); // Expect empty result
    }

    @Test
    public void testTranslate_OutOfBounds() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int index = unescaper.translate("&#;", 0, result); // No character after '&#' causing out of bounds error
        assertEquals(0, index); // Expect no translation
        assertEquals("", result.toString()); // Expect empty result
    }

    @Test
    public void testTranslate_NoNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder result = new StringBuilder();
        int index = unescaper.translate("&;", 0, result); // No numeric entity
        assertEquals(0, index); // Expect no translation
        assertEquals("", result.toString()); // Expect empty result
    }
}