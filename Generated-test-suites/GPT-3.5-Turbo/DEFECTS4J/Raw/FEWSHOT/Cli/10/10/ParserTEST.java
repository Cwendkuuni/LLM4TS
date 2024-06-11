package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
                return null;
            }
        };
    }

    @Test
    public void testParse1() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[0];

        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test
    public void testParse2() throws ParseException {
        Options options = new Options();
        String[] arguments = null;

        CommandLine result = parser.parse(options, arguments);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testParse3() throws ParseException {
        Options options = new Options();
        options.addOption("a");

        String[] arguments = new String[] { "-b" };

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions() throws MissingOptionException {
        parser.checkRequiredOptions();
    }

    @Test
    public void testProcessProperties1() {
        Properties properties = null;

        parser.processProperties(properties);
    }

    @Test
    public void testProcessProperties2() {
        Properties properties = new Properties();
        properties.setProperty("a", "b");
        properties.setProperty("c", "d");

        parser.processProperties(properties);
        assertTrue(parser.getCommandLine().hasOption("a"));
        assertTrue(parser.getCommandLine().hasOption("c"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", false, "test option");

        ListIterator<String> iterator = Arrays.asList("value", "non-option").listIterator();

        parser.processArgs(option, iterator);

        assertEquals(1, option.getValues().length);
        assertEquals("value", option.getValue());
        assertEquals("non-option", iterator.next());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissingArg() throws ParseException {
        Option option = new Option("a", true, "test option");

        ListIterator<String> iterator = Arrays.asList("-a").listIterator();

        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Option option = new Option("a", false, "test option");
        Options options = new Options();
        options.addOption(option);

        ListIterator<String> iterator = Arrays.asList("value", "non-option").listIterator();

        parser.setOptions(options);
        parser.processOption("a", iterator);

        assertTrue(parser.getCommandLine().hasOption("a"));
        assertEquals(1, option.getValues().length);
        assertEquals("value", option.getValue());
        assertEquals("non-option", iterator.next());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        Option option = new Option("a", false, "test option");
        Options options = new Options();
        options.addOption(option);

        ListIterator<String> iterator = Arrays.asList("value", "non-option").listIterator();

        parser.setOptions(options);
        parser.processOption("b", iterator);
    }

}