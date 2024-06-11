package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
    
    @Test
    public void testSetOptions() {
        parser.setOptions(options);
        
        assertEquals(options, parser.getOptions());
        assertNull(parser.getRequiredOptions());
    }
    
    @Test
    public void testParseWithNullArguments() throws ParseException {
        CommandLine cmd = parser.parse(options, null);
        
        assertEquals(new CommandLine(), cmd);
    }
    
    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"-f", "value"};
        CommandLine cmd = parser.parse(options, arguments);
        
        assertEquals(new CommandLine(), cmd);
    }
    
    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("f", "value");
        
        CommandLine cmd = parser.parse(options, null, properties);
        
        assertEquals(new CommandLine(), cmd);
    }
    
    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] arguments = {"-f", "value"};
        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertEquals(new CommandLine(), cmd);
    }
    
    @Test
    public void testParseWithOptionsArgumentsAndStopAtNonOption() throws ParseException {
        String[] arguments = {"-f", "value"};
        CommandLine cmd = parser.parse(options, arguments, true);
        
        assertEquals(new CommandLine(), cmd);
    }
    
    @Test
    public void testProcessPropertiesWithNullProperties() {
        parser.processProperties(null);
        
        // Nothing should happen
    }
    
    @Test
    public void testCheckRequiredOptionsWithNoRequiredOptions() throws MissingOptionException {
        parser.setOptions(options);
        parser.checkRequiredOptions();
        
        // Nothing should happen
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsWithMissingOptions() throws MissingOptionException {
        Option option = new Option("f", "foo", false, "Option");
        options.addOption(option);
        
        parser.setOptions(options);
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("f", "foo", true, "Option");
        ListIterator<String> iterator = Arrays.asList("value").listIterator();
        
        parser.processArgs(option, iterator);
        
        assertEquals("value", option.getValue());
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = new Option("f", "foo", true, "Option");
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        parser.processArgs(option, iterator);
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("f", "foo", true, "Option");
        ListIterator<String> iterator = Arrays.asList("value").listIterator();
        
        parser.processOption("f", iterator);
        
        assertEquals(new CommandLine().addOption(option), cmd);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        ListIterator<String> iterator = Arrays.asList().listIterator();
        
        parser.processOption("f", iterator);
    }
}