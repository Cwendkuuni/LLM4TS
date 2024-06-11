import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateBasic() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Test &#65;"; // Numeric entity for "A"
        
        try {
            unescaper.translate(input, 5, output);
            Assert.assertEquals("Test A", output.toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Test &#x41;"; // Numeric entity for "A" in hex
       
        try {
            unescaper.translate(input, 5, output);
            Assert.assertEquals("Test A", output.toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateEdgeCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Test &#100000;"; // Numeric entity larger than 0xFFFF
       
        try {
            unescaper.translate(input, 5, output);
            Assert.assertEquals("Test 𐄀", output.toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Test &#XYZ;"; // Invalid numeric entity
        
        try {
            unescaper.translate(input, 5, output);
            Assert.fail("Should have thrown NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTranslateNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "Test &#65"; // Numeric entity without ending semi-colon
        
        try {
            unescaper.translate(input, 5, output);
            Assert.assertEquals("Test A", output.toString());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

}