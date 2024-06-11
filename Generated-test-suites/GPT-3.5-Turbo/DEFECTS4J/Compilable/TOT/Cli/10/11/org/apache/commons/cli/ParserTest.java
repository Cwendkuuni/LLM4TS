package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new ParserImpl();
        options = new Options();
    }

    @Test
    public void testParse_WithValidOptionsAndArguments_ReturnsCommandLineObject() {
        String[] arguments = {"-a", "arg1", "--option", "arg2"};
        options.addOption("a", false, "Option a");
        options.addOption("option", true, "Option with argument");
        
        try {
            CommandLine result = parser.parse(options, arguments);
            
            assertTrue(result.hasOption("a"));
            assertEquals("arg1", result.getArgList().get(0));
            assertTrue(result.hasOption("option"));
            assertEquals("arg2", result.getOptionValue("option"));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithInvalidOption_ThrowsUnrecognizedOptionException() {
        String[] arguments = {"-a"};
        
        try {
            parser.parse(options, arguments);
            fail("UnrecognizedOptionException should be thrown");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -a", e.getMessage());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithMissingRequiredOption_ThrowsMissingOptionException() {
        String[] arguments = {"-a", "arg1"};
        options.addOption("a", true, "Required Option a");
        options.addOption("b", true, "Required Option b");
        
        try {
            parser.parse(options, arguments);
            fail("MissingOptionException should be thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: b", e.getMessage());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithOptionWithNoArgumentAndStopAtNonOption_ReturnsCommandLineObject() {
        String[] arguments = {"-a", "--", "nonOptionArg1", "nonOptionArg2"};
        options.addOption("a", false, "Option a");
        
        try {
            CommandLine result = parser.parse(options, arguments, true);
            
            assertTrue(result.hasOption("a"));
            assertEquals("nonOptionArg1", result.getArgList().get(0));
            assertEquals("nonOptionArg2", result.getArgList().get(1));
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithOptionWithNoArgumentAndStopAtNonOptionAndNoNonOptionArg_ReturnsCommandLineObject() {
        String[] arguments = {"-a", "--"};
        options.addOption("a", false, "Option a");
        
        try {
            CommandLine result = parser.parse(options, arguments, true);
            
            assertTrue(result.hasOption("a"));
            assertEquals(0, result.getArgList().size());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithOptionWithArgumentAndStopAtNonOption_ThrowsMissingArgumentException() {
        String[] arguments = {"-a", "arg1", "nonOptionArg"};
        options.addOption("a", true, "Option a");
        
        try {
            parser.parse(options, arguments, true);
            fail("MissingArgumentException should be thrown");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option:a", e.getMessage());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    @Test
    public void testParse_WithOptionWithArgumentAndStopAtNonOptionAndNoNonOptionArg_ReturnsCommandLineObject() {
        String[] arguments = {"-a", "arg1", "--"};
        options.addOption("a", true, "Option a");
        
        try {
            CommandLine result = parser.parse(options, arguments, true);
            
            assertTrue(result.hasOption("a"));
            assertEquals("arg1", result.getOptionValue("a"));
            assertEquals(0, result.getArgList().size());
        } catch (ParseException e) {
            fail("ParseException should not be thrown");
        }
    }

    private class ParserImpl extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return null;
        }
    }
}