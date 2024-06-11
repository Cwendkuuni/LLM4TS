import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    
    private Parser parser = new Parser() {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return new String[0];
        }
    };
    
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        
        String[] args = new String[]{"-a", "-b", "value"};
        
        CommandLine cmd = parser.parse(options, args);
        
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("value", cmd.getOptionValue("b"));
    }
    
    @Test
    public void testParseWithMissingRequiredOption() {
        Options options = new Options();
        options.addOption(Option.builder("a")
                .required(true)
                .build());
        
        String[] args = new String[0];
        
        try {
            parser.parse(options, args);
            fail("Should throw MissingOptionException");
        } catch (MissingOptionException e) {
            assertTrue(e.getMessage().contains("Missing required option(s): a"));
        }
    }
    
    @Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        
        String[] args = new String[]{"-b"};
        
        try {
            parser.parse(options, args);
            fail("Should throw UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertTrue(e.getMessage().contains("Unrecognized option: -b"));
        }
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option A");
        
        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertTrue(option.hasValue());
        assertEquals(2, option.getValues().length);
        assertEquals("value1", option.getValues()[0]);
        assertEquals("value2", option.getValues()[1]);
    }
    
    @Test
    public void testProcessArgsWithMissingArgument() {
        Option option = new Option("a", true, "Option A");
        
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        try {
            parser.processArgs(option, iterator);
            fail("Should throw MissingArgumentException");
        } catch (MissingArgumentException e) {
            assertTrue(e.getMessage().contains("Missing argument for option: a"));
        }
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        
        Option option = options.getOption("a");
        
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        parser.processOption("a", iterator);
        
        assertTrue(option.hasValue());
        assertEquals("", option.getValue());
    }
    
    @Test
    public void testProcessOptionWithUnrecognizedOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        try {
            parser.processOption("b", iterator);
            fail("Should throw UnrecognizedOptionException");
        } catch (UnrecognizedOptionException e) {
            assertTrue(e.getMessage().contains("Unrecognized option: b"));
        }
    }
}