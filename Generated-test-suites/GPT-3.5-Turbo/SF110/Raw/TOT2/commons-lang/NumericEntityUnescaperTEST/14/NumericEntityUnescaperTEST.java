import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaper_Constructor() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertNotNull(unescaper);
    }

    @Test
    public void testNumericEntityUnescaper_isSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testNumericEntityUnescaper_translate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        char[] input = {'&', '#', 'x', '0', '1', '2', ';'};
        StringBuilder output = new StringBuilder();
        int result = unescaper.translate(input, 0, new StringWriter(output));
        
        assertEquals(6, result);
        assertEquals('�', output.toString().charAt(0));
    }

    @Test
    public void testNumericEntityUnescaper_optionErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        
        char[] input = {'&', '#', '1', '2'};
        StringBuilder output = new StringBuilder();
        
        try {
            unescaper.translate(input, 0, new StringWriter(output));
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }
    
    // Additional test cases for other methods and edge cases can be added here
    
}