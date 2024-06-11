package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.ParseOptions;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParseWithoutOptionsAndArguments() throws ParseException {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("Expected empty CommandLine.", cmd.getArgs().size(), 0);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B");
        String[] arguments = {"--optionA", "arg1", "--optionB", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("Expected option A to be present.", 1, cmd.getOptions().size());
        Option optionA = cmd.getOption("a");
        Assert.assertTrue("Expected option A to be set.", optionA.isSet());

        Assert.assertEquals("Expected 2 args.", cmd.getArgs().size(), 2);
        Assert.assertEquals("Expected arg1.", cmd.getArgs()[0], "arg1");
        Assert.assertEquals("Expected arg2.", cmd.getArgs()[1], "arg2");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        options.addOption("a", true, "option A");
        String[] arguments = {"--missingOption"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        options.addOption("a", true, "option A");
        String[] arguments = {"--unrecognizedOption"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        options.addOption("a", true, "option A");
        String[] arguments = {"--optionA", "arg1", "-n", "arg3"};
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals("Expected 2 options.", cmd.getOptions().size(), 1);
        Option optionA = cmd.getOption("a");
        Assert.assertTrue("Expected option A to be set.", optionA.isSet());

        Assert.assertEquals("Expected 2 args.", cmd.getArgs().size(), 2);
        Assert.assertEquals("Expected arg1.", cmd.getArgs()[0], "arg1");
        Assert.assertEquals("Expected arg2.", cmd.getArgs()[1], "arg3");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        String[] arguments = {"--optionA", "--optionB"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        options.addOption("a", true, "option A");
        options.addRequiredOption("b", false, "option B");
        String[] arguments = {"--optionA"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithOptionsGroup() throws ParseException {
        OptionGroup group = new OptionGroup("group1");
        group.setRequired(false);
        options.addOptionGroup(group);
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B", group);
        options.addOption("c", false, "option C", group);
        String[] arguments = {"--group1", "--optionB", "arg1", "--optionA"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("Expected 1 option in the group.", cmd.getOptions(group).size(), 2);
        Assert.assertEquals("Expected option B to be present.", 1, cmd.getOptions(group).get(0).countOccurrences());
        Assert.assertEquals("Expected option C to be present.", 1, cmd.getOptions(group).get(1).countOccurrences());
    }
}