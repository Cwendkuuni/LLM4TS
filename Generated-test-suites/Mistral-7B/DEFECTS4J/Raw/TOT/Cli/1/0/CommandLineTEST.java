package org.apache.commons.cli.test;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CommandLine} class.
 */
public class CommandLineTest {

    private CommandLine commandLine;
    private String[] arguments;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addBooleanOption("booleanOption", "b", false);
        options.addOption("stringOption", "s");
        options.addOption("intOption", "i", true, "10");
        commandLine = new CommandLine();
        arguments = new String[]{"--booleanOption", "true", "--intOption", "5"};
    }

    @Test
    public void testHasOption() {
        commandLine.parse(options, arguments);
        assertTrue(commandLine.hasOption("booleanOption"));
        assertTrue(commandLine.hasOption('b'));
        assertFalse(commandLine.hasOption("nonexistentOption"));
        assertFalse(commandLine.hasOption('n'));
    }

    @Test(expected = AssertionError.class)
    public void testHasOptionThrowsExceptionWhenOptionIsNotPresent() {
        commandLine.parse(options, new String[0]);
        assertTrue(commandLine.hasOption("booleanOption")); // should not reach here
    }

    @Test
    public void testGetOptionValue() {
        commandLine.parse(options, arguments);
        assertNull(commandLine.getOptionValue("nonexistentOption"));
        assertNull(commandLine.getOptionValue('n'));
        assertTrue(commandLine.getOptionValue("booleanOption").equals("true"));
        assertTrue(commandLine.getOptionValue('b').equals("true"));
        assertNull(commandLine.getOptionValue("stringOption"));
        assertNull(commandLine.getOptionValue('s'));
        assertTrue(commandLine.getOptionValue("intOption").equals("5"));
        assertTrue(commandLine.getOptionValue('i').equals("5"));
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValueThrowsExceptionWhenOptionIsNotPresent() {
        commandLine.parse(options, new String[0]);
        assertNull(commandLine.getOptionValue("booleanOption")); // should not reach here
    }

    @Test
    public void testGetOptionValues() {
        options.addOption("stringOption", "s", true);
        arguments = new String[]{"--stringOption", "value1", "--stringOption", "value2"};
        commandLine.parse(options, arguments);
        String[] values = commandLine.getOptionValues("stringOption");
        assertNull(values);
        assertSame(2, values.length);
        assertTrue(values[0].equals("value1"));
        assertTrue(values[1].equals("value2"));
    }

    @Test
    public void testGetOptionValuesEmpty() {
        commandLine.parse(options, new String[0]);
        assertNull(commandLine.getOptionValues("booleanOption"));
        assertNull(commandLine.getOptionValues('b'));
        assertNull(commandLine.getOptionValues("nonexistentOption"));
        assertNull(commandLine.getOptionValues('n'));
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWithUnexecutedOptions() throws ParseException {
        options.addOption("stringOption", "s", true);
        arguments = new String[]{"--stringOption", "value1"};
        commandLine.parse(options, arguments);
        commandLine.parse(options, new String[]{"--stringOption", "value2"}); // should throw exception
    }
}