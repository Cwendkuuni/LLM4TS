import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test input without numeric entities
        String input = "This is a test";
        StringBuilder output = new StringBuilder();
        try {
            int result = unescaper.translate(input, 0, output);
            Assert.assertEquals(0, result);
            Assert.assertEquals("This is a test", output.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        
        // Test input with numeric entities in decimal form
        input = "This is a &#104;test";
        output = new StringBuilder();
        try {
            int result = unescaper.translate(input, 0, output);
            Assert.assertEquals(6, result);
            Assert.assertEquals("This is a htest", output.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        
        // Test input with numeric entities in hexadecimal form
        input = "This is a &#x68;test";
        output = new StringBuilder();
        try {
            int result = unescaper.translate(input, 0, output);
            Assert.assertEquals(6, result);
            Assert.assertEquals("This is a htest", output.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        
        // Test input with numeric entities without semicolon
        input = "This is a &#104 test";
        output = new StringBuilder();
        try {
            int result = unescaper.translate(input, 0, output);
            Assert.assertEquals(7, result);
            Assert.assertEquals("This is a h  test", output.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
        
        // Test input with invalid numeric entity
        input = "This is a &#-104;test";
        output = new StringBuilder();
        try {
            int result = unescaper.translate(input, 0, output);
            Assert.assertEquals(0, result);
            Assert.assertEquals("This is a &#-104;test", output.toString());
        } catch (Exception e) {
            Assert.fail("Unexpected exception");
        }
    }
}