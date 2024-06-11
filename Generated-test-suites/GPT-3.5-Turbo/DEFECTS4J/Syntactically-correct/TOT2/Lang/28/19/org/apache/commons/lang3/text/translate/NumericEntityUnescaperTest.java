import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate_NormalDecimal() {
        // Normal decimal numeric entity
        StringBuilder output = new StringBuilder();
        try {
            int index = numericEntityUnescaper.translate("&#65;", 0, output);
            Assert.assertEquals(6, index); // Expected output value for character "A"
        } catch (IOException e) {
            Assert.fail("IOException caught while testing translate_NormalDecimal");
        }
    }

    @Test
    public void testTranslate_Hexadecimal() {
        // Hexadecimal numeric entity
        StringBuilder output = new StringBuilder();
        try {
            int index = numericEntityUnescaper.translate("&#x3a;", 0, output);
            Assert.assertEquals(7, index); // Expected output value for character ":"
        } catch (IOException e) {
            Assert.fail("IOException caught while testing translate_Hexadecimal");
        }
    }

    @Test
    public void testTranslate_InvalidEntity() {
        // Invalid numeric entity
        StringBuilder output = new StringBuilder();
        try {
            int index = numericEntityUnescaper.translate("&#invalid;", 0, output);
            Assert.assertEquals(0, index); // Expected output value for invalid entity
        } catch (IOException e) {
            Assert.fail("IOException caught while testing translate_InvalidEntity");
        }
    }

    @Test
    public void testTranslate_ExceptionHandling() {
        // Exception handling for NumberFormatException
        StringBuilder output = new StringBuilder();
        try {
            int index = numericEntityUnescaper.translate("&#xg;", 0, output);
            Assert.assertEquals(0, index); // Expected output value for invalid hex number
        } catch (IOException e) {
            Assert.fail("IOException caught while testing translate_ExceptionHandling");
        }
    }
}