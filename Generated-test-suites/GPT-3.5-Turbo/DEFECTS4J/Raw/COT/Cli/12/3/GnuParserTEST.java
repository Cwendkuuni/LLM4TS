import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {
  
    @Test
    public void testFlatten() {
        // Create Options object
        Options options = new Options();
        
        // Add options to the object
        
        // Test case 1: No options provided
        String[] arguments1 = {};
        String[] expected1 = {};
        assertArrayEquals(expected1, GnuParser.flatten(options, arguments1, false));
        
        // Test case 2: Options provided, no non-option encountered
        String[] arguments2 = {"-a", "value1", "--option", "value2"};
        String[] expected2 = {"-a", "value1", "--option", "value2"};
        assertArrayEquals(expected2, GnuParser.flatten(options, arguments2, false));
        
        // Test case 3: Non-option encountered, stopAtNonOption is true
        String[] arguments3 = {"-a", "value1", "nonOption", "--option", "value2"};
        String[] expected3 = {"-a", "value1", "nonOption"};
        assertArrayEquals(expected3, GnuParser.flatten(options, arguments3, true));
        
        // Test case 4: Non-option encountered, stopAtNonOption is false
        String[] arguments4 = {"-a", "value1", "nonOption", "--option", "value2"};
        String[] expected4 = {"-a", "value1", "nonOption", "--option", "value2"};
        assertArrayEquals(expected4, GnuParser.flatten(options, arguments4, false));
        
        // Test case 5: Stop marker encountered, should exclude remaining arguments
        String[] arguments5 = {"-a", "--", "-b"};
        String[] expected5 = {"-a", "--"};
        assertArrayEquals(expected5, GnuParser.flatten(options, arguments5, false));
        
        // Test case 6: Single dash encountered, should not be included as option
        String[] arguments6 = {"-"};
        String[] expected6 = {"-"};
        assertArrayEquals(expected6, GnuParser.flatten(options, arguments6, false));
    }
}