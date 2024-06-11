import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();

        // Create options here
        Option option1 = Option.builder("a")
                             .desc("Option 1")
                             .build();
        
        Option option2 = Option.builder("b")
                             .argName("arg")
                             .desc("Option 2")
                             .hasArg()
                             .build();
        
        options.addOption(option1);
        options.addOption(option2);
    }

    @Test
    public void testFlatten_WithValidOption_ReturnsCorrectFlattenedArray() {
        String[] arguments = {"-a", "-b", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 3", 3, result.length);
        assertEquals("Expected element at index 0", "-a", result[0]);
        assertEquals("Expected element at index 1", "-b", result[1]);
        assertEquals("Expected element at index 2", "value", result[2]);
    }

    @Test
    public void testFlatten_WithInvalidOptionAndStopAtNonOption_ReturnsCorrectFlattenedArray() {
        String[] arguments = {"-c", "-a", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 4", 4, result.length);
        assertEquals("Expected element at index 0", "-c", result[0]);
        assertEquals("Expected element at index 1", "--", result[1]);
        assertEquals("Expected element at index 2", "-a", result[2]);
        assertEquals("Expected element at index 3", "value", result[3]);
    }

    @Test
    public void testFlatten_WithInvalidOptionAndContinueFlattening_ReturnsCorrectFlattenedArray() {
        String[] arguments = {"-c", "-a", "value"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals("Expected length of 3", 3, result.length);
        assertEquals("Expected element at index 0", "-c", result[0]);
        assertEquals("Expected element at index 1", "-a", result[1]);
        assertEquals("Expected element at index 2", "value", result[2]);
    }

    @Test
    public void testFlatten_WithDoubleHyphen_DoesNotProcessRemainingTokens() {
        String[] arguments = {"-a", "--", "-b", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 3", 3, result.length);
        assertEquals("Expected element at index 0", "-a", result[0]);
        assertEquals("Expected element at index 1", "--", result[1]);
        assertEquals("Expected element at index 2", "-b", result[2]);
    }

    @Test
    public void testFlatten_WithSpecialToken_HandlesTokenCorrectly() {
        String[] arguments = {"--option=value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 2", 2, result.length);
        assertEquals("Expected element at index 0", "--option", result[0]);
        assertEquals("Expected element at index 1", "value", result[1]);
    }

    @Test
    public void testFlatten_WithSingleHyphen_HandlesTokenCorrectly() {
        String[] arguments = {"-"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 1", 1, result.length);
        assertEquals("Expected element at index 0", "-", result[0]);
    }

    @Test
    public void testFlatten_WithOptionWithoutArgument_HandlesTokenCorrectly() {
        String[] arguments = {"-a"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 1", 1, result.length);
        assertEquals("Expected element at index 0", "-a", result[0]);
    }

    @Test
    public void testFlatten_WithOptionAndArgument_HandlesTokenCorrectly() {
        String[] arguments = {"-b", "value"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals("Expected length of 2", 2, result.length);
        assertEquals("Expected element at index 0", "-b", result[0]);
        assertEquals("Expected element at index 1", "value", result[1]);
    }
    
}