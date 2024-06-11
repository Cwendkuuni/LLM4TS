package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                // implementation not needed for testing
                return null;
            }
        };
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", false, "Option c");

        String[] arguments = {"-a", "-b", "value", "arg1", "arg2"};

        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("a"));
        assertEquals("value", result.getOptionValue("b"));
        assertTrue(result.hasOption("c"));
        assertEquals(2, result.getArgs().length);
        assertEquals("arg1", result.getArgs()[0]);
        assertEquals("arg2", result.getArgs()[1]);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "-b"};

        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"-a"};

        parser.parse(options, arguments);
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        Properties properties = new Properties();
        properties.put("a", "true");
        properties.put("b", "value");

        parser.setOptions(options);
        parser.processProperties(properties);

        assertTrue(parser.cmd.hasOption("a"));
        assertEquals("value", parser.cmd.getOptionValue("b"));
    }

    @Test
    public void testCheckRequiredOptionsWithMissingOption() {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        parser.setOptions(options);
        parser.cmd = new CommandLine();

        try {
            parser.checkRequiredOptions();
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a, b", e.getMessage());
        }
    }

    @Test
    public void testCheckRequiredOptionsWithAllOptions() throws MissingOptionException {
        Options options = new Options();
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");

        parser.setOptions(options);
        parser.cmd = new CommandLine();
        parser.cmd.addOption(new Option("a", "value"));
        parser.cmd.addOption(new Option("b", "value"));

        parser.checkRequiredOptions();

        assertTrue(parser.getRequiredOptions().isEmpty());
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", true, "Option a");

        ListIterator iter = new ArrayList<String>().listIterator();
        iter.add("value");

        parser.processArgs(option, iter);

        assertNotNull(option.getValues());
        assertEquals("value", option.getValue());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsWithMissingArgument() throws ParseException {
        Option option = new Option("a", true, "Option a");

        ListIterator iter = new ArrayList<String>().listIterator();

        parser.processArgs(option, iter);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option a");

        ListIterator iter = new ArrayList<String>().listIterator();
        iter.add("-a");
        iter.add("value");

        parser.setOptions(options);
        parser.cmd = new CommandLine();

        parser.processOption("a", iter);

        assertTrue(parser.cmd.hasOption("a"));
        assertEquals("value", parser.cmd.getOptionValue("a"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionWithUnrecognizedOption() throws ParseException {
        Options options = new Options();

        ListIterator iter = new ArrayList<String>().listIterator();

        parser.setOptions(options);
        parser.processOption("b", iter);
    }
}