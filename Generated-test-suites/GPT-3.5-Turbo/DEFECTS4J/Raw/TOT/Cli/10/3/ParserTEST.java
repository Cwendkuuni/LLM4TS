import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    private Options options;
    private Parser parser;
    
    @Before
    public void setUp() {
        options = new Options();
        parser = new ParserImpl();
    }
    
    @Test
    public void testParse_RequiredOptionsPresent_Success() throws ParseException {
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-a", "value", "-b"};
        
        CommandLine result = parser.parse(options, arguments);
        
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("a"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionsMissing_ExceptionThrown() throws ParseException {
        Option option1 = new Option("a", "option1", true, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");
        
        option1.setRequired(true);
        option2.setRequired(true);
        
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-a", "value"};
        
        parser.parse(options, arguments);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ExceptionThrown() throws ParseException {
        Option option = new Option("a", "option", false, "Option");
        
        options.addOption(option);
        
        String[] arguments = {"-b"};
        
        parser.parse(options, arguments);
    }
    
    // Test cases for other methods in the class
    
}