import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        try {
            // Test with input containing numeric entities
            StringBuilder input = new StringBuilder("This is a test &#65; &#x41;");
            StringBuilder expectedOutput = new StringBuilder("This is a test A A");
            StringBuilder actualOutput = new StringBuilder();
            
            int translatedLength = unescaper.translate(input, 0, actualOutput);
            
            assertEquals("Translated length is incorrect", 22, translatedLength);
            assertEquals("Translated output is incorrect", expectedOutput.toString(), actualOutput.toString());
            
            // Test with input not containing numeric entities
            input = new StringBuilder("This is a test");
            expectedOutput = new StringBuilder("This is a test");
            actualOutput = new StringBuilder();
            
            translatedLength = unescaper.translate(input, 0, actualOutput);
            
            assertEquals("Translated length is incorrect", 0, translatedLength);
            assertEquals("Translated output is incorrect", expectedOutput.toString(), actualOutput.toString());
            
            // Test with incomplete numeric entity
            input = new StringBuilder("This is a test &#");
            expectedOutput = new StringBuilder("This is a test &#");
            actualOutput = new StringBuilder();
            
            translatedLength = unescaper.translate(input, 0, actualOutput);
            
            assertEquals("Translated length is incorrect", 0, translatedLength);
            assertEquals("Translated output is incorrect", expectedOutput.toString(), actualOutput.toString());
            
            // Test with invalid numeric entity
            input = new StringBuilder("This is a test &#invalid;");
            expectedOutput = new StringBuilder("This is a test &#invalid;");
            actualOutput = new StringBuilder();
            
            translatedLength = unescaper.translate(input, 0, actualOutput);
            
            assertEquals("Translated length is incorrect", 0, translatedLength);
            assertEquals("Translated output is incorrect", expectedOutput.toString(), actualOutput.toString());
        } catch (Exception e) {
            fail("Caught unexpected exception: " + e.getMessage());
        }
    }
}