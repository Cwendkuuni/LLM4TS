package org.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsAndNoArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        CommandLine commandLine = parser.parse(options, new String[0]);
        Assert.assertEquals(new CommandLine(), commandLine);
    }

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] arguments = {"--a", "--b=1", "arg1", "arg2"};
        CommandLine commandLine = parser.parse(options, arguments);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("1", commandLine.getOptionValue("b"));
        Assert.assertEquals(2, commandLine.getArgList().size());
        Assert.assertEquals("arg1", commandLine.getArgList().get(0));
        Assert.assertEquals("arg2", commandLine.getArgList().get(1));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", false, "Option B");
        String[] arguments = {"--a"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgumentForOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, true, "Option B");
        String[] arguments = {"--a"};
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--c"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseStopAtNonOption() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        String[] arguments = {"--a", "-", "--b=1", "arg1", "arg2"};
        parser.parse(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMultipleUnrecognizedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--a", "--x", "--y"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, true, "Option B");
        Properties properties = new Properties();
        properties.setProperty("a", "valueA");
        properties.setProperty("b", "valueB");
        CommandLine commandLine = parser.parse(options, new String[0], properties, false);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertEquals("valueA", commandLine.getOptionValue("a"));
        Assert.assertEquals("valueB", commandLine.getOptionValue("b"));
    }
}