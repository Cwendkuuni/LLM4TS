
import static org.junit.Assert.*;
import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    
    private Parser parser;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[] {};
            }
        };
    }
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        String[] arguments = {};
        CommandLine cmd = parser.parse(options, arguments);
        
        // Check if all options are parsed correctly
        assertEquals(options, parser.getOptions());
        assertNull(cmd);
        
        // Check if required options are checked correctly
        options.addOption(Option.builder("a").required().build());
        
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }
    
    @Test
    public void testProcessProperties() {
        Properties properties = new Properties();
        properties.put("a", "1");
        properties.put("b", "text");
        
        parser.processProperties(properties);
        
        // Check if options from properties are added to the command line
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertTrue(parser.getCommandLine().hasOption("b"));
        
        // Check if values are added to the options
        Option optionA = parser.getOptions().getOption("a");
        Option optionB = parser.getOptions().getOption("b");
        
        assertEquals("1", optionA.getValue());
        assertEquals("text", optionB.getValue());
        
        // Check if additional options from properties are ignored when not found in options
        properties.put("c", "true");
        
        try {
            parser.processProperties(properties);
            fail("UnrecognizedOptionException should have been thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: c", e.getMessage());
        }
    }
    
    @Test
    public void testCheckRequiredOptions() {
        Options options = new Options();
        
        Option optionA = Option.builder("a").required().build();
        options.addOption(optionA);
        
        Option optionB = Option.builder("b").required().build();
        options.addOption(optionB);
        
        parser.setOptions(options);
        
        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required options: a, b", e.getMessage());
        }
        
        parser.getRequiredOptions().remove(optionB.getKey());
        
        try {
            parser.checkRequiredOptions();
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
        
        parser.getRequiredOptions().remove(optionA.getKey());
        
        try {
            parser.checkRequiredOptions();
            assertEquals(0, parser.getRequiredOptions().size());
        } catch (MissingOptionException e) {
            fail("No MissingOptionException should have been thrown");
        }
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = Option.builder("a").argName("arg").hasArgs().build();
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
        
        Option optionalOption = Option.builder("b").optionalArg(true).build();
        ListIterator<String> optionalIterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(optionalOption, optionalIterator);
        
        assertEquals("value1", optionalOption.getValue());
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option option = Option.builder("a").argName("arg").hasArgs().build();
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processOption("a", iterator);
        
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertEquals(option, parser.getCommandLine().getOption("a"));
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
        
        Option unrecognizedOption = Option.builder("b").build();
        ListIterator<String> unrecognizedIterator = Arrays.asList("value1").listIterator();
        
        try {
            parser.processOption("b", unrecognizedIterator);
            fail("UnrecognizedOptionException should have been thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: b", e.getMessage());
        }
    }
}
