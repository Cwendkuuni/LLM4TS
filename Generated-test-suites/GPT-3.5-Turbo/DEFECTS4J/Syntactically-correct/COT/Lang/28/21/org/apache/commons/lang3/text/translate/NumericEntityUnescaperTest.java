
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test basic functionality
        // Input: "&#97;",&#97; translates to 'a'
        // Expected output: 'a'
        StringBuilder input1 = new StringBuilder("&#97;");
        StringBuilder output1 = new StringBuilder();
        try {
            assertEquals(6, unescaper.translate(input1, 0, output1));
            assertEquals("a", output1.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Add more test cases here to cover different scenarios
        
    }
    
    // Add test cases for each public method here
    
    // Test for edge cases and exception handling scenarios
    @Test
    public void testTranslate_edgeCases() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test empty input
        // Expected output: 0
        StringBuilder input1 = new StringBuilder("");
        StringBuilder output1 = new StringBuilder();
        try {
            assertEquals(0, unescaper.translate(input1, 0, output1));
            assertEquals("", output1.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test input with no numeric entity
        // Input: "abc"
        // Expected output: 0
        StringBuilder input2 = new StringBuilder("abc");
        StringBuilder output2 = new StringBuilder();
        try {
            assertEquals(0, unescaper.translate(input2, 0, output2));
            assertEquals("", output2.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Test input with invalid numeric entity
        // Input: "&#a;"
        // Expected output: 0
        StringBuilder input3 = new StringBuilder("&#a;");
        StringBuilder output3 = new StringBuilder();
        try {
            assertEquals(0, unescaper.translate(input3, 0, output3));
            assertEquals("", output3.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }

        // Add more edge cases and exception handling scenarios here
        
    }

    @Test
    public void testTranslate_exceptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test IOException handling
        StringBuilder input1 = new StringBuilder("&#97;");
        StringBuilder output1 = new StringBuilder();
        try {
            unescaper.translate(input1, 0, null);  // Should throw IOException
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected exception
        }
        
        // Add more exception handling scenarios here
        
    }
    
}
