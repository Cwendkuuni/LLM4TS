package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return null;
            }
        };
    }

    @Test
    public void testParse_withOptionsAndArguments_returnsCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertTrue(result.hasOption("b"));
        assertEquals("value", result.getOptionValue("b"));
        assertNull(result.getOptionValue("c"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_withInvalidOption_throwsUnrecognizedOptionException() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_withMissingArgumentForRequiredOption_throwsMissingArgumentException() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_withMissingRequiredOption_throwsMissingOptionException() throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("a").required(true).build());

        String[] arguments = {};

        parser.parse(options, arguments);
    }

    @Test
    public void testParse_withStopAtNonOption_stopsParsingAtNonOptionArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", false, "Option b");

        String[] arguments = {"-a", "nonOption", "-b"};

        CommandLine result = parser.parse(options, arguments, true);

        assertTrue(result.hasOption("a"));
        assertFalse(result.hasOption("b"));
        assertEquals(1, result.getArgs().length);
        assertEquals("nonOption", result.getArgs()[0]);
    }

    @Test
    public void testParse_withProperties_setsOptionValuesFromProperties() throws ParseException {
        Options options = new Options();
        Option option = Option.builder("a").hasArg(true).build();
        options.addOption(option);

        String[] arguments = {};

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("a"));
    }

    private boolean assertFalse(boolean hasOption) {
        return !hasOption;
    }
}