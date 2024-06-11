import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test basic functionality
        String input = "&#65;";
        StringBuilder result = new StringBuilder();
        unescaper.translate(input, 0, result);
        String expected = "A";
        assertEquals(expected, result.toString());
        
        // Test hexadecimal input
        input = "&#x41;";
        result = new StringBuilder();
        unescaper.translate(input, 0, result);
        expected = "A";
        assertEquals(expected, result.toString());
        
        // Test no semi-colon required
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        input = "&#65";
        result = new StringBuilder();
        unescaper.translate(input, 0, result);
        expected = "";
        assertEquals(expected, result.toString());
        
        // Test error if no semi-colon
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        input = "&#65";
        result = new StringBuilder();
        try {
            unescaper.translate(input, 0, result);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test invalid input
        input = "&#notanumber;";
        result = new StringBuilder();
        unescaper.translate(input, 0, result);
        expected = "";
        assertEquals(expected, result.toString());
    }
}