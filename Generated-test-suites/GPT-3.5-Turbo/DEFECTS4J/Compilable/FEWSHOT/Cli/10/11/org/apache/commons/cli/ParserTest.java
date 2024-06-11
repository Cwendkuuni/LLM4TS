package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Options options;
    
    @Before
    public void setUp() {
        options = new Options();
    }
    
    @Test
    public void testSetOptions() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        
        parser.setOptions(options);
        assertEquals(options, parser.getOptions());
        assertEquals(options.getRequiredOptions(), parser.getRequiredOptions());
    }
    
    @Test
    public void testFlatten() {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        String[] result = parser.flatten(options, new String[0], false);
        assertEquals(0, result.length);
    }
    
    @Test
    public void testParse() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgList().isEmpty());
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[] {"arg1", "arg2"};
            }
        };
        
        Option option = new Option("o", true, "option");
        parser.processArgs(option, new java.util.LinkedList<String>().listIterator());
        
        assertEquals("arg1", option.getValue());
        assertEquals(2, option.getValues().length);
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArgument() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        Option option = new Option("o", true, "option");
        parser.processArgs(option, new java.util.LinkedList<String>().listIterator());
    }
    
    @Test
    public void testProcessOption() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        Option option = new Option("o", true, "option");
        options.addOption(option);
        
        parser.processOption("o", new java.util.LinkedList<String>().listIterator());
        assertTrue(parser.cmd.hasOption("o"));
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognizedOption() throws ParseException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        parser.processOption("o", new java.util.LinkedList<String>().listIterator());
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        Parser parser = new Parser() {
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return new String[0];
            }
        };
        
        Option option = new Option("o", true, "option");
        option.setRequired(true);
        options.addOption(option);
        
        parser.checkRequiredOptions();
    }
}