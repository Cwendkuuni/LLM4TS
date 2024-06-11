import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {
    
    @Test
    public void testParseWithNullArguments() throws ParseException {
        // Arrange
        Options options = new Options();
        Parser parser = new ParserImpl();
        
        // Act
        CommandLine result = parser.parse(options, null);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        // Arrange
        Options options = new Options();
        Parser parser = new ParserImpl();
        
        // Act
        CommandLine result = parser.parse(options, new String[0]);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("t", "test", false, "Test option");
        options.addOption(option);
        Parser parser = new ParserImpl();
        String[] arguments = {"-t"};
        
        // Act
        CommandLine result = parser.parse(options, arguments);
        
        // Assert
        assertTrue(result.hasOption("t"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        // Arrange
        Options options = new Options();
        Parser parser = new ParserImpl();
        String[] arguments = {"-u"};
        
        // Act
        parser.parse(options, arguments);
        
        // Assert
        // Exception expected
    }
    
    @Test
    public void testParseWithRequiredOption() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new ParserImpl();
        String[] arguments = {"-r", "value"};
        
        // Act
        CommandLine result = parser.parse(options, arguments);
        
        // Assert
        assertTrue(result.hasOption("r"));
        assertEquals("value", result.getOptionValue("r"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option = new Option("r", "required", true, "Required option");
        option.setRequired(true);
        options.addOption(option);
        Parser parser = new ParserImpl();
        String[] arguments = {};
        
        // Act
        parser.parse(options, arguments);
        
        // Assert
        // Exception expected
    }
    
    @Test
    public void testParseWithMultipleOptionsAndArguments() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        Option option3 = new Option("c", "option3", true, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "-b", "value1", "-c", "value2"};
        
        // Act
        CommandLine result = parser.parse(options, arguments);
        
        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertTrue(result.hasOption("c"));
        assertEquals("value1", result.getOptionValue("b"));
        assertEquals("value2", result.getOptionValue("c"));
    }
    
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        // Arrange
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new ParserImpl();
        String[] arguments = {"-a", "-b", "value1", "nonoption"};
        
        // Act
        CommandLine result = parser.parse(options, arguments, true);
        
        // Assert
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertFalse(result.hasOption("nonoption"));
        assertEquals("value1", result.getOptionValue("b"));
    }
    
    private class ParserImpl extends Parser {
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }
}