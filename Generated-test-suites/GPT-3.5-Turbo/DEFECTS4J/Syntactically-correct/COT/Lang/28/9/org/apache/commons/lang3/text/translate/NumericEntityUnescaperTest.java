import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper numericEntityUnescaper = new NumericEntityUnescaper();
        
        // Test basic functionality
        StringBuilder input = new StringBuilder("&amp;");
        StringBuilder output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate(input, 0, output);
            assertEquals(2, result);
            assertEquals("&", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test Hexadecimal entity
        input = new StringBuilder("&#x41;");
        output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate(input, 0, output);
            assertEquals(6, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test Non-hexadecimal entity
        input = new StringBuilder("&#65;");
        output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate(input, 0, output);
            assertEquals(5, result);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test invalid entity
        input = new StringBuilder("&#XYZ;");
        output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test partial entity
        input = new StringBuilder("&#");
        output = new StringBuilder();
        try {
            int result = numericEntityUnescaper.translate(input, 0, output);
            assertEquals(0, result);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
}


