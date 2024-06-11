package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
    }

    @Test
    public void testParseWithEmptyArgs() throws ParseException {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleArg() throws ParseException {
        String[] args = {"arg1"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithMultipleArgs() throws ParseException {
        String[] args = {"arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
        Assert.assertEquals("arg2", cmd.getArgs().get(1));
        Assert.assertEquals("arg3", cmd.getArgs().get(2));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        String[] args = {"--optArg", "value"};
        options.addOption("optArg", true, "optArg desc");
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("optArg"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredArg() throws ParseException {
        String[] args = {"--optArg"};
        options.addOption("optArg", true, "optArg desc");
        parser.parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        String[] args = {"--unknownOption"};
        CommandLine cmd = parser.parse(options, args);
        Assert.fail("An UnrecognizedOptionException should have been thrown");
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        String[] args = {"--option1", "--option2"};
        options.addOption("option1", true, "option1 desc");
        options.addOption("option2", true, "option2 desc");
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
    }

    @Test
    public void testParseWithDoubleDash() throws ParseException {
        String[] args = {"--"};
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {};
        Properties properties = new Properties();
        properties.setProperty("optArg", "value");
        CommandLine cmd = parser.parse(options, null, properties);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("value", cmd.getOptionValue("optArg"));
    }
}