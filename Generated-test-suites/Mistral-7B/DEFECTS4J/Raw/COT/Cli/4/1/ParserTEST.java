package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParser_Basic() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParse_RequiredOptions() throws ParseException {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Option A is required", e.getMessage());
        }
    }

    @Test
    public void testParse_OptionsGroups() throws ParseException {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "My group");
        group.addOption(new Option("option1", true, "Option 1"));
        group.addOption(new Option("option2", true, "Option 2"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
    }

    @Test
    public void testParse_UnrecognizedOptions() throws ParseException {
        String[] args = new String[]{"--unknownOption", "--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: --unknownOption", e.getMessage());
        }
    }

    @Test
    public void testParse_MixedOptionsAndArguments() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals(2, cmd.getArgs().length);
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParse_DoubleDash() throws ParseException {
        String[] args = new String[]{"--"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasArgs());
        Assert.assertFalse(cmd.hasOption(""));
    }

    @Test
    public void testParse_EmptyArgumentsArray() throws ParseException {
        String[] args = new String[0];
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_NullArgumentsArray() throws ParseException {
        String[] args = null;
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParse_OptionalArgWithoutValue() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A with optional arg");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(null, cmd.getOptionValue("a"));
    }

    @Test
    public void testParse_OptionalArgWithValue() throws ParseException {
        String[] args = new String[]{"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Option A with optional arg");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParse_MultipleValuesForOptionalArg() throws ParseException {
        String[] args = new String[]{"-a", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A with optional arg");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        String[] values = cmd.getOptionValues("a");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingValueForOptionalArg() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A with optional arg");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertArrayEquals(new String[0], cmd.getOptionValues("a"));

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingOption() throws ParseException {
        String[] args = new String[]{"--missingOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: --missingOption", e.getMessage());
        }
    }

    @Test
    public void testParse_Help() throws ParseException {
        String[] args = new String[]{"--help"};
        Options options = new Options();
        options.addOption("h", "help", false, "display this help message and exit");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java MyApp", options);
        System.exit(0);
    }
}