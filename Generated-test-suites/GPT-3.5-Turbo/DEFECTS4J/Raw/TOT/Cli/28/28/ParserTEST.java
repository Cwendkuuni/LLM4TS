import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
        
        options = new Options();
    }
    
    // Test parse method with typical use case
    @Test
    public void testParse_typicalUseCase() throws ParseException {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
    }
    
    // Test parse method with edge case of empty arguments
    @Test
    public void testParse_emptyArguments() throws ParseException {
        String[] args = {};
        
        CommandLine cmd = parser.parse(options, args);
        
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }
    
    // Test parse method with error scenario of unrecognized option
    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_unrecognizedOption() throws ParseException {
        String[] args = {"-c"};
        options.addOption("a", false, "Option A")
               .addOption("b", true, "Option B");
        
        parser.parse(options, args);
    }
    
    // Test parse method with error scenario of missing argument
    @Test(expected = MissingArgumentException.class)
    public void testParse_missingArgument() throws ParseException {
        String[] args = {"-b"};
        options.addOption("a", false, "Option A")
               .addOption("b", true, "Option B");
        
        parser.parse(options, args);
    }
    
    // Test processOption method with typical use case of option with argument
    @Test
    public void testProcessOption_optionWithArgument() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = Arrays.asList("arg1").listIterator();
        
        parser.processOption("a", iterator);
        
        assertTrue(option.hasArg());
        assertEquals("arg1", option.getValue());
    }
    
    // Test processOption method with typical use case of option without argument
    @Test
    public void testProcessOption_optionWithoutArgument() throws ParseException {
        Option option = new Option("a", false, "Option A");
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processOption("a", iterator);
        
        assertFalse(option.hasArg());
    }
    
    // Test processOption method with edge case of unrecognized option
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_unrecognizedOption() throws ParseException {
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processOption("c", iterator);
    }
    
    // Test checkRequiredOptions method with typical use case of no missing options
    @Test
    public void testCheckRequiredOptions_noMissingOptions() throws MissingOptionException {
        options.addOption("a", false, "Option A").isRequired();
        options.addOption("b", true, "Option B");
        
        parser.setOptions(options);
        
        parser.checkRequiredOptions();
    }
    
    // Test checkRequiredOptions method with error scenario of missing options
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_missingOptions() throws MissingOptionException {
        options.addOption("a", false, "Option A").isRequired();
        options.addOption("b", true, "Option B").isRequired();
        
        parser.setOptions(options);
        
        parser.checkRequiredOptions();
    }
    
    // Test processProperties method with typical use case of non-optional argument option
    @Test
    public void testProcessProperties_nonOptionalArgumentOption() throws ParseException {
        Option option = new Option("a", true, "Option A");
        Properties properties = new Properties();
        properties.setProperty("a", "arg1");
        
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        
        parser.processProperties(properties);
        
        assertTrue(cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
    }
    
    // Test processProperties method with typical use case of optional boolean option
    @Test
    public void testProcessProperties_optionalBooleanOption() throws ParseException {
        Option option = new Option("a", false, "Option A");
        Properties properties = new Properties();
        properties.setProperty("a", "true");
        
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        
        parser.processProperties(properties);
        
        assertTrue(cmd.hasOption("a"));
    }
    
    // Test processProperties method with edge case of unknown option
    @Test
    public void testProcessProperties_unknownOption() throws ParseException {
        Option option = new Option("a", false, "Option A");
        Properties properties = new Properties();
        properties.setProperty("b", "arg1");
        
        CommandLine cmd = new CommandLine();
        cmd.addOption(option);
        
        parser.processProperties(properties);
        
        assertFalse(cmd.hasOption("b"));
    }
    
    // Test processArgs method with typical use case of single argument option
    @Test
    public void testProcessArgs_singleArgumentOption() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = Arrays.asList("arg1").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertEquals("arg1", option.getValues()[0]);
    }
    
    // Test processArgs method with edge case of missing argument for option
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument() throws ParseException {
        Option option = new Option("a", true, "Option A");
        ListIterator iterator = Arrays.asList().listIterator();
        
        parser.processArgs(option, iterator);
    }
    
}