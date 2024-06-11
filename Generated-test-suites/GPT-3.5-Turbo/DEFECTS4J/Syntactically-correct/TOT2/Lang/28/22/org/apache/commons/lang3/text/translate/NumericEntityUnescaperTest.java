import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Typical use case
        StringBuilder output = new StringBuilder();
        try {
            int index = unescaper.translate("&#65;", 0, output);
            Assert.assertEquals(6, index);
            Assert.assertEquals('A', output.toString().charAt(0));
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        
        // Edge cases
        try {
            output.setLength(0); // Clear the output
            int index = unescaper.translate("&#x44;", 0, output);
            Assert.assertEquals(6, index);
            Assert.assertEquals('D', output.toString().charAt(0));
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        
        // Error scenario
        output.setLength(0); // Clear the output
        try {
            int index = unescaper.translate("&;", 0, output);
            Assert.assertEquals(0, index);
            Assert.assertEquals(0, output.length());
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
}