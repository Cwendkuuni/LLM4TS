package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        // Test empty arguments
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        String[] expected = new String[0];
        String[] result = parser.flatten(new Options(), arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test no options
        Options options = new Options();
        arguments = new String[]{"arg1", "arg2"};
        expected = new String[]{"arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test single option
        options.addOption("o", "option", true, "Option value");
        arguments = new String[]{"--option", "value"};
        expected = new String[]{"option", "value"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test option with no value
        arguments = new String[]{"--option"};
        expected = new String[]{"option"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test option with multiple values
        options.addOption("o", "option", true, "Option value");
        arguments = new String[]{"--option", "value1", "--option", "value2"};
        expected = new String[]{"option", "value1", "option", "value2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test long option
        options.addOption("lo", "longOption", true, "Long option value");
        arguments = new String[]{"--longOption", "value"};
        expected = new String[]{"longOption", "value"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test stopAtNonOption
        options.addOption("o", "option", true, "Option value");
        arguments = new String[]{"arg1", "--option", "value", "arg3"};
        expected = new String[]{"arg1", "--option", "value"};
        result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);

        // Test multiple options
        options.addOption("o1", "option1", true, "Option1 value");
        options.addOption("o2", "option2", true, "Option2 value");
        arguments = new String[]{"--o1", "value1", "--o2", "value2"};
        expected = new String[]{"option1", "value1", "option2", "value2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test bursting
        options.addOption("o", "option", true, "Option value");
        arguments = new String[]{"-o", "arg1", "-o", "arg2"};
        expected = new String[]{"option", "arg1", "option", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test long option with value
        options.addOption("lo", "longOption", true, "Long option value");
        arguments = new String[]{"--longOption=value"};
        expected = new String[]{"longOption", "value"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test long option with multiple values
        options.addOption("lo", "longOption", true, "Long option value");
        arguments = new String[]{"--longOption=value1", "--longOption=value2"};
        expected = new String[]{"longOption", "value1", "longOption", "value2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);

        // Test long option with no value
        options.addOption("lo", "longOption");
        arguments = new String[]{"--longOption"};
        expected = new String[]{"longOption"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlattenWithException() {
        // Test invalid option
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] arguments = new String[]{"--invalid"};
        try {
            parser.flatten(options, arguments, false);
        } catch (ParseException e) {
            List expected = Arrays.asList("invalid", "--invalid");
            List result = Arrays.asList("-", "--invalid");
            Assert.assertEquals(expected, parser.getTokens());
            throw e;
        }
    }

    // Add more test cases if necessary
}