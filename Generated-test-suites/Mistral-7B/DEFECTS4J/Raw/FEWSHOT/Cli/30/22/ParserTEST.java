package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.util.StringArrayBuilder;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"--foo", "-b", "-a", "arg1", "arg2"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);

        Option fooOption = new Option("f", "foo", false, "Description for foo");
        Option aOption = new Option("a", "arg", true, "Description for arg");
        Option bOption = new Option("b", "bar", false, "Description for bar");
        options.addOption(fooOption);
        options.addOption(bOption);
        options.addOption(aOption);

        assertEquals(Arrays.asList(fooOption, bOption, aOption), commandLine.getOptions());
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getArgs());
        assertTrue(commandLine.hasOption("foo"));
        assertTrue(commandLine.hasOption("arg"));
        assertFalse(commandLine.hasOption("non_exist"));
    }

    @Test
    public void testParseWithHelp() throws ParseException {
        String[] args = {"--help"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);

        Option helpOption = new Option("h", "help", false, "Print this help message.");
        options.addOption(helpOption);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", "", options, true);
        fail("Should print help message.");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"--bar"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option barOption = new Option("b", "bar", true, "Description for bar");
        Option fooOption = new Option("f", "foo", true, "Description for foo");
        options.addOption(fooOption);
        options.addOption(barOption);

        CommandLine commandLine = parser.parse(options, args, false);
        assertNull(commandLine.getOptionValue("bar"));
        assertTrue(commandLine.hasOption("foo"));
        throw new MissingOptionException("The following required options are missing: [bar]");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option aOption = new Option("a", "arg", true, "Description for arg");
        options.addOption(aOption);

        CommandLine commandLine = parser.parse(options, args, false);
        throw new MissingArgumentException(aOption, "No value provided for option 'a'.");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] args = {"--long-option"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option longOption = new Option("l", "long-option", false, "Description for long-option");
        options.addOption(longOption);

        CommandLine commandLine = parser.parse(options, args, true);
        assertTrue(commandLine.hasOption("long-option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown-option"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        Option aOption = new Option("a", "arg", true, "Description for arg");
        options.addOption(aOption);

        CommandLine commandLine = parser.parse(options, args, false);
        throw new UnrecognizedOptionException("Unrecognized option: --unknown-option");
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        String[] args = {"--group", "-a", "-b", "arg1", "arg2"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "Group options");
        Option aOption = new Option("a", "arg", true, "Description for arg A");
        Option bOption = new Option("b", "arg", true, "Description for arg B");
        group.addOption(aOption);
        group.addOption(bOption);
        options.addOption(group);

        CommandLine commandLine = parser.parse(options, args, true);
        Option groupOption = commandLine.getOption("group");
        assertEquals(Arrays.asList(aOption, bOption), groupOption.getValues());
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithNotRecognizedGroup() throws ParseException {
        String[] args = {"--not_group", "-a", "-b", "arg1", "arg2"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "Group options");
        Option aOption = new Option("a", "arg", true, "Description for arg A");
        Option bOption = new Option("b", "arg", true, "Description for arg B");
        group.addOption(aOption);
        group.addOption(bOption);
        options.addOption(new Option("g", "group", false, "Description for group"));
        options.addOption(group);

        CommandLine commandLine = parser.parse(options, args, true);
        throw new ParseException("Unrecognized option: --not_group");
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredGroup() throws ParseException {
        String[] args = {"--group"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "Group options");
        Option aOption = new Option("a", "arg", true, "Description for arg A");
        Option bOption = new Option("b", "arg", true, "Description for arg B");
        group.addOption(aOption);
        group.addOption(bOption);
        options.addOption(group);

        CommandLine commandLine = parser.parse(options, args, true);
        throw new MissingOptionException("The following required option groups are missing: [group]");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidGroup() throws ParseException {
        String[] args = {"--group"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        OptionGroup group = new OptionGroup("group", "Group options");
        options.addOption(group);

        CommandLine commandLine = parser.parse(options, args, true);
        throw new ParseException("Invalid group: group");
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"-Dproperty1=value1", "-Dproperty2=value2"};
        Properties properties = new Properties();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(null, args, properties, true);

        assertEquals(Arrays.asList("-Dproperty1=value1", "-Dproperty2=value2"), commandLine.getArgs());
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testParseWithInvalidProperty() throws ParseException {
        String[] args = {"-Dproperty=value"};
        Properties properties = new Properties();
        CommandLineParser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(null, args, properties, true);
        throw new ParseException("Invalid property: property");
    }
}