package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        String[] args = new String[]{"--foo", "--bar=abc", "-b", "arg1", "-x", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, false);

        List<Option> options = cmd.getOptions();

        Option fooOpt = new Option("f", "foo", false, "foo description");
        Option barOpt = new Option("b", "bar", true, "bar description");
        Option xOpt = new Option("x", "x", false, "x description");

        assertTrue(options.contains(fooOpt));
        assertTrue(options.contains(barOpt));
        assertTrue(options.contains(xOpt));
        assertSame(cmd.getOptionValue("foo"), "");
        assertSame(cmd.getOptionValues("bar"), new String[]{"abc"});
        assertSame(cmd.getOptionValues("x"), new String[0]);

        assertTrue(cmd.getArgs().size() == 2);
        assertSame(cmd.getArgs().get(0), "arg1");
        assertSame(cmd.getArgs().get(1), "arg2");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws Exception {
        String[] args = new String[]{"-a", "arg1", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("b", "b", true, "b description")), args, false);

        assertFalse(cmd.hasOption("b"));
        throw new AssertionError("Should throw MissingOptionException");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionValue() throws Exception {
        String[] args = new String[]{"--foo=arg1", "--bar", "arg3"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("f", "foo", true, "foo description")).addOption(new Option("b", "bar", true, "bar description")), args, false);

        Option fooOpt = cmd.getOption("foo");
        Option barOpt = cmd.getOption("bar");

        assertSame(fooOpt.getValue(), "arg1");
        assertNull(barOpt.getValue());

        throw new AssertionError("Should throw MissingArgumentException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--qux", "--foo", "--bar=abc"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("f", "foo", false, "foo description")).addOption(new Option("b", "bar", true, "bar description")), args, false);

        List<Option> options = cmd.getOptions();

        assertFalse(options.contains(new Option("q", "qux", false, "qux description")));

        throw new AssertionError("Should throw UnrecognizedOptionException");
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--foo=arg1", "--qux=arg2", "--bar=arg3"};

        Properties argsProperties = new Properties();
        argsProperties.setProperty("qux", "arg2_prop");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("f", "foo", false, "foo description")).addOption(new Option("b", "bar", true, "bar description")), args, argsProperties, false);

        List<Option> options = cmd.getOptions();

        Option fooOpt = cmd.getOption("foo");
        Option barOpt = cmd.getOption("bar");
        Option quxOpt = cmd.getOption("qux");

        assertSame(fooOpt.getValue(), "arg1");
        assertSame(barOpt.getValue(), "arg3");
        assertSame(quxOpt.getValue(), "arg2_prop");
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        String[] args = new String[]{"--foo", "--bar=abc", "--group", "arg1", "--group", "arg2"};

        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.addOption(new Option("g", "group", false, "group description"));

        Option fooOpt = new Option("f", "foo", false, "foo description");

        Options options = new Options();
        options.addOption(fooOpt).addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        List<Option> optionsList = cmd.getOptions();

        assertTrue(optionsList.contains(fooOpt));
        assertTrue(optionsList.contains(group));
        Option groupOption = optionsList.get(optionsList.indexOf(group));
        assertTrue(groupOption.isSelected());

        List<Option> groupOptions = groupOption.getOptions();
        assertTrue(groupOptions.size() == 2);
        assertTrue(groupOptions.get(0).isSelected());
        assertTrue(groupOptions.get(1).isSelected());

        Option groupArg1 = groupOptions.get(0);
        Option groupArg2 = groupOptions.get(1);

        assertSame(groupArg1.getValue(), "arg1");
        assertSame(groupArg2.getValue(), "arg2");
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = new String[]{"--foo", "arg1", "-x", "--bar=abc", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("f", "foo", false, "foo description")).addOption(new Option("x", "x", false, "x description")).addOption(new Option("b", "bar", true, "bar description")), args, false, true);

        List<Option> options = cmd.getOptions();

        Option fooOpt = cmd.getOption("foo");
        Option xOpt = cmd.getOption("x");
        Option barOpt = cmd.getOption("bar");

        assertSame(fooOpt.getValue(), "arg1");
        assertSame(xOpt.getValue(), "");
        assertSame(barOpt.getValue(), "abc");

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.size() == 2);
        assertSame(argsList.get(0), "arg2");
    }

    @Test
    public void testParseWithInvalidStopAtNonOption() throws Exception {
        String[] args = new String[]{"--foo", "arg1", "-x", "--bar=abc", "arg2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options().addOption(new Option("f", "foo", false, "foo description")).addOption(new Option("x", "x", false, "x description")).addOption(new Option("b", "bar", true, "bar description")), args, false, true);

        List<Option> options = cmd.getOptions();

        Option fooOpt = cmd.getOption("foo");
        Option xOpt = cmd.getOption("x");
        Option barOpt = cmd.getOption("bar");

        assertSame(fooOpt.getValue(), "arg1");
        assertSame(xOpt, null);
        assertSame(barOpt.getValue(), "abc");

        List<String> argsList = cmd.getArgs();
        assertTrue(argsList.size() > 2);
        throw new AssertionError("Should not contain more than two args");
    }
}