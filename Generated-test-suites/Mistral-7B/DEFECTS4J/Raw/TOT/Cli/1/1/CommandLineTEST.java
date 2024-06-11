package org.apache.commons.cli.test;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test case for CommandLine class.
 */
public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "arg1", true, "an argument for option a");
        options.addOption("o", "opt", false, "an option without argument");
    }

    /**
     * Test hasOption method with valid options.
     */
    @Test
    public void testHasOptionWithValidOptions() {
        String[] args = {"-a", "arg1", "-o"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("o"));
        assertFalse(commandLine.hasOption("x"));
    }

    /**
     * Test hasOption method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testHasOptionWithInvalidOptions() {
        String[] args = {"--x"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        commandLine.hasOption("x");
    }

    /**
     * Test getOptionValue method with valid options.
     */
    @Test
    public void testGetOptionValueWithValidOptions() {
        String[] args = {"-a", "arg1", "-o"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        assertSame("arg1", commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue("x"));
    }

    /**
     * Test getOptionValue method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testGetOptionValueWithInvalidOptions() {
        String[] args = {"--x"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        commandLine.getOptionValue("x");
    }

    /**
     * Test getOptionValues method with valid options.
     */
    @Test
    public void testGetOptionValuesWithValidOptions() {
        String[] args = {"--arg1=value1", "--arg1=value2"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        String[] values = commandLine.getOptionValues("arg1");

        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");

        assertSame(expectedValues.toArray(new String[0]), values);
    }

    /**
     * Test getOptionValues method with invalid options.
     */
    @Test(expected = ParseException.class)
    public void testGetOptionValuesWithInvalidOptions() {
        String[] args = {"--x"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        commandLine.getOptionValues("x");
    }

    /**
     * Test getArgs method.
     */
    @Test
    public void testGetArgs() {
        String[] args = {"arg1", "arg2"};
        commandLine = new CommandLine();
        commandLine.parse(options, args);

        String[] expectedArgs = {"arg1", "arg2"};

        assertSame(expectedArgs, commandLine.getArgs());
    }
}