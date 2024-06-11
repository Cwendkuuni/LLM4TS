package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Tests for the Parser class.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithEmptyArguments() throws Exception {
        String[] arguments = new String[0];
        CommandLine expected = new CommandLine();
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithSingleArgument() throws Exception {
        String[] arguments = new String[]{"arg1"};
        CommandLine expected = new CommandLine();
        expected.addArg("arg1");
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithSingleOption() throws Exception {
        options.addOption("option1", true, "o");
        String[] arguments = new String[]{"-o"};
        CommandLine expected = new CommandLine();
        expected.addOption(options.getOption("option1"));
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithSingleOptionValue() throws Exception {
        options.addOption("option1", true, "o");
        String[] arguments = new String[]{"-o", "value"};
        CommandLine expected = new CommandLine();
        expected.addOption(options.getOption("option1"), "value");
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithMultipleOptions() throws Exception {
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "p");
        String[] arguments = new String[]{"-o", "-p"};
        CommandLine expected = new CommandLine();
        expected.addOption(options.getOption("option1"));
        expected.addOption(options.getOption("option2"));
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithMultipleOptionsValues() throws Exception {
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "p");
        String[] arguments = new String[]{"-o", "value1", "-p", "value2"};
        CommandLine expected = new CommandLine();
        expected.addOption(options.getOption("option1"), "value1");
        expected.addOption(options.getOption("option2"), "value2");
        CommandLine actual = parser.parse(options, arguments);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWithRequiredOptions() throws Exception {
        options.addRequiredOption("option1", false, "o");
        String[] arguments = new String[0];
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            String[] expected = new String[]{"option1"};
            Assert.assertArrayEquals(expected, e.getMissingOptions());
        }

        options.addOption("option2", true, "p");
        arguments = new String[]{"-o"};
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            String[] expected = new String[]{"option2"};
            Assert.assertArrayEquals(expected, e.getMissingOptions());
        }

        String[] args = new String[]{"-o", "value1", "-o", "value2"};
        CommandLine expectedCmd = new CommandLine();
        expectedCmd.addOption(options.getOption("option1"));
        expectedCmd.addOption(options.getOption("option1"), "value2");
        CommandLine actualCmd = parser.parse(options, args);
        Assert.assertEquals(expectedCmd, actualCmd);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        options.addOption("option1", true, "o");
        options.addOption("option2", true, "p");
        options.addOption("option3", true, "q");
        options.getOption("option3").setArgName("name");
        String[] arguments = new String[]{"-o", "-p"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2.option3", "value3");

        options.addOption("option1", true, "o");
        options.addOption("option2.option3", true, "p");

        CommandLine actual = parser.parse(options, null, properties);
        CommandLine expected = new CommandLine();
        expected.addOption(options.getOption("option1"), "value1");
        expected.addOption(options.getOption("option2.option3"), "value3");
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        options.addOption("option1", true, "o");
        String[] arguments = new String[]{"-x"};
        parser.parse(options, arguments);
    }
}