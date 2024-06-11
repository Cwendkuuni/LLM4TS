import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new ConcreteParser();
    }
    
    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", "alpha", false, "Set option alpha");
        options.addOption("b", "bravo", true, "Set option bravo with value");
        String[] arguments = {"-a", "-b", "value"};
        
        CommandLine result = parser.parse(options, arguments);
        
        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals(result.getOptionValue("b"), "value");
    }
    
    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("c", "charlie", false, "Set option charlie");
        Properties properties = new Properties();
        properties.setProperty("c", "true");
        
        CommandLine result = parser.parse(options, new String[0], properties);
        
        assertTrue(result.hasOption("c"));
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOptions() {
        Options options = new Options();
        Option requiredOption = new Option("r", "required", true, "Required option");
        requiredOption.setRequired(true);
        options.addOption(requiredOption);
        
        parser.parse(options, new String[0]);
    }
    
    @Test
    public void testProcessArgs() {
        Option option = new Option("o", "option", true, "Option with value");
        ListIterator iterator = Arrays.asList("-o", "value").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertTrue(option.hasArgs());
        assertEquals(option.getValue(), "value");
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingValue() {
        Option option = new Option("o", "option", true, "Option with value");
        ListIterator iterator = Arrays.asList("-o").listIterator();
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() {
        Option option = new Option("o", "option", false, "Simple option");
        ListIterator iterator = Arrays.asList("-o").listIterator();
        
        parser.processOption("o", iterator);
        
        assertTrue(cmd.hasOption("o"));
    }

}