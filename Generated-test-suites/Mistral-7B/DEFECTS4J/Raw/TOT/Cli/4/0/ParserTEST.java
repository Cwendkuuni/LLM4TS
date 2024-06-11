package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Parser class.
 */
public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs().toArray(new String[0]));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] args = {"--long-option-a", "arg1", "--long-option-b=arg2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "long option A");
        options.addOption("longOptionB", true, "long option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs().toArray(new String[0]));
        Assert.assertTrue(cmd.hasOption("longOptionA"));
        Assert.assertTrue(cmd.hasOption("longOptionB"));
        Assert.assertEquals("arg2", cmd.getOptionValue("longOptionB"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = {"--foo"};
        Options options = new Options();
        options.addRequiredOption("bar", true, "required option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail(); // should not reach this point
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail(); // should not reach this point
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--prop1=val1", "--prop2=val2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "val1");
        properties.setProperty("prop2", "val2");

        Options options = new Options();
        options.addOptions(Parser.parseProperties(properties));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertNull(cmd.getArgs());
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("prop1"));
        Assert.assertTrue(cmd.hasOption("prop2"));
        Assert.assertEquals("val1", cmd.getOptionValue("prop1"));
        Assert.assertEquals("val2", cmd.getOptionValue("prop2"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = {"--foo", "--bar"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(false);
        group.addOption(new Option("foo", false, null, "option foo"));
        group.addOption(new Option("bar", false, null, "option bar"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("foo"));
        Assert.assertTrue(cmd.hasOption("bar"));
        Assert.assertFalse(cmd.hasOption("group"));
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] args = {"--foo", "--bar=arg1", "-a", "arg2", "-b=arg3"};
        Options options = new Options();
        options.addOption("foo", true, "option foo");
        options.addOption("bar", true, "option bar");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertArrayEquals(new String[]{"arg2", "arg3"}, cmd.getArgs().toArray(new String[0]));
        Assert.assertTrue(cmd.hasOption("foo"));
        Assert.assertTrue(cmd.hasOption("bar"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("arg1", cmd.getOptionValue("bar"));
    }
}