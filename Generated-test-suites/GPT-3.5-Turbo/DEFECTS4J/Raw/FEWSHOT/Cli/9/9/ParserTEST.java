import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParse() {
        // create options
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "option1 description");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option2);
        
        // create arguments
        String[] arguments = {"-a", "value1", "-b"};
        
        // create parser
        Parser parser = new DefaultParser();
        
        // parse the arguments
        CommandLine cmd = parser.parse(options, arguments);
        
        // check if options are present
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        
        // check option values
        assertEquals("value1", cmd.getOptionValue("a"));
        
        // check if non-option arguments are present
        assertEquals(0, cmd.getArgs().length);
    }
    
    @Test
    public void testParseWithProperties() {
        // create options
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "option1 description");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option2);
        
        // create arguments
        String[] arguments = {"-a", "value1", "-b"};
        
        // create properties
        Properties properties = new Properties();
        properties.setProperty("c", "value2");
        properties.setProperty("d", "true");
        
        // create parser
        Parser parser = new DefaultParser();
        
        // parse the arguments with properties
        CommandLine cmd = parser.parse(options, arguments, properties);
        
        // check if options are present
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        
        // check option values
        assertEquals("value1", cmd.getOptionValue("a"));
        assertEquals("value2", cmd.getOptionValue("c"));
        
        // check if non-option arguments are present
        assertEquals(0, cmd.getArgs().length);
    }
    
    @Test
    public void testParseWithStopAtNonOption() {
        // create options
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "option1 description");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option2);
        
        // create arguments
        String[] arguments = {"-a", "value1", "non-option", "-b"};
        
        // create parser
        Parser parser = new DefaultParser();
        
        // parse the arguments with stop at non-option
        CommandLine cmd = parser.parse(options, arguments, true);
        
        // check if options are present
        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        
        // check option values
        assertEquals("value1", cmd.getOptionValue("a"));
        
        // check if non-option arguments are present
        assertEquals("non-option", cmd.getArgs()[0]);
    }
    
    @Test
    public void testParseWithOptionsNotPresent() {
        // create options
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "option1 description");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", false, "option2 description");
        options.addOption(option2);
        
        // create arguments
        String[] arguments = {"-c", "value1"};
        
        // create parser
        Parser parser = new DefaultParser();
        
        // parse the arguments
        try {
            parser.parse(options, arguments);
            fail("Should throw UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -c", e.getMessage());
        }
    }
    
    @Test
    public void testParseWithMissingOptionValues() {
        // create options
        Options options = new Options();
        Option option1 = new Option("a", "option1", true, "option1 description");
        options.addOption(option1);
        Option option2 = new Option("b", "option2", true, "option2 description");
        options.addOption(option2);
        
        // create arguments
        String[] arguments = {"-a"};
        
        // create parser
        Parser parser = new DefaultParser();
        
        // parse the arguments
        try {
            parser.parse(options, arguments);
            fail("Should throw MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        }
    }
}