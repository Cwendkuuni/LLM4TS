package org.apache.commons.cli;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_shouldReturnEmptyArrayForEmptyArguments() {
        String[] arguments = {};
        String[] result = parser.flatten(options, arguments, true);
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_shouldReturnSameArrayForNoOptions() {
        String[] arguments = {"arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(arguments, result);
    }

    @Test
    public void testFlatten_shouldReturnFlattenedArrayForOptions() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", true, "Option b");
        Option option3 = new Option("c", false, "Option c");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        String[] arguments = {"-a", "-b", "value", "-c", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "-b", "value", "-c", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_shouldStopAtNonOptionIfSpecified() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "nonOption", "-b"};
        String[] result = parser.flatten(options, arguments, true);

        String[] expected = {"-a", "nonOption", "-b"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_shouldNotStopAtNonOptionIfNotSpecified() {
        Option option1 = new Option("a", false, "Option a");
        Option option2 = new Option("b", false, "Option b");

        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-a", "nonOption", "-b"};
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-a", "-b"};
        assertArrayEquals(expected, result);
    }
}
