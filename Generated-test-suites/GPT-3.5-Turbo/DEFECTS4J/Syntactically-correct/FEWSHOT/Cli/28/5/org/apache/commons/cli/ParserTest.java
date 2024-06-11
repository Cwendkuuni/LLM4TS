package org.apache.commons.cli;

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
                return arguments;
            }
        };
        options = new Options();
    }

    @Test
    public void testParse_withOptionsAndArguments_shouldReturnCommandLine() throws ParseException {
        String[] args = {"-a", "-b", "value", "--option", "arg1", "arg2"};
        options.addOption("a", false, "Description for option a");
        options.addOption("b", true, "Description for option b");
        options.addOption("option", true, "Description for option option");

        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertTrue(result.hasOption("option"));
        assertNotNull(result.getOptionValue("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_withMissingRequiredOption_shouldThrowMissingOptionException() throws ParseException {
        String[] args = {"-a"};
        options.addRequiredOption("a", null, "Required option a");

        parser.parse(options, args);
    }

    @Test
    public void testParse_withOptionalArgumentValue_shouldAddValueToOption() throws ParseException {
        String[] args = {"-a", "value"};
        options.addOption("a", true, "Optional argument option a");

        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_withMissingArgumentValue_shouldThrowMissingArgumentException() throws ParseException {
        String[] args = {"-a"};
        options.addOption("a", true, "Optional argument option a");

        parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_withUnrecognizedOption_shouldThrowUnrecognizedOptionException() throws ParseException {
        String[] args = {"-a"};
        options.addOption("b", false, "Description for option b");

        parser.parse(options, args);
    }

    @Test
    public void testParse_withStopAtNonOptionTrue_shouldStopParsingAtNonOption() throws ParseException {
        String[] args = {"-a", "nonOption", "-b"};
        options.addOption("a", false, "Description for option a");
        options.addOption("b", false, "Description for option b");

        CommandLine result = parser.parse(options, args, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test
    public void testParse_withProperties_shouldSetOptionValuesFromProperties() throws ParseException {
        String[] args = {};
        options.addOption("a", true, "Description for option a");
        OptionsParser properties = new OptionsParser();
        properties.addOption("a", "value");

        CommandLine result = parser.parse(options, args, properties);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

}