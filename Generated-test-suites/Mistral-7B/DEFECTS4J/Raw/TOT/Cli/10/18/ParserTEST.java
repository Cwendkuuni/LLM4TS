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
 * Test class for {@link Parser}.
 */
public class ParserTest {

    @Test
    public void testParseSimple() throws Exception {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("Option A should be set to true", true, cmd.hasOption("a"));
        Assert.assertEquals("Argument for option A should be equal to 1", "1", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception {
        String[] args = {"--non-existent"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test
    public void testMissingValue() throws Exception {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertFalse("Option A should not be set to a value", cmd.hasOption("a", true));
    }

    @Test
    public void testValueWithSpace() throws Exception {
        String[] args = {"-a", "value with space"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("Argument for option A should be equal to value with space", "value with space", cmd.getOptionValue("a"));
    }

    @Test
    public void testMissingRequiredOption() throws Exception {
        String[] args = {"-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args, false);
            Assert.fail("MissingRequiredOptionException should be thrown");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = {"--foo", "bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "baz");

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("Option Foo should be set to baz", "baz", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithLongOption() throws Exception {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("Value for long option should be equal to value", "value", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testParseWithOptionGroup() throws Exception {
        String[] args = {"--group", "option1"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group of options");
        group.setRequired(false);
        group.addOption(new Option("option1", true, "Option 1"));
        group.addOption(new Option("option2", true, "Option 2"));

        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue("Option 1 should be present", cmd.hasOption("option1"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = {"--non-existent"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithExtraArguments() throws Exception {
        String[] args = {"-a", "1", "--non-existent"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args, false);
            Assert.fail("ParseException should be thrown");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: non-existent", e.getMessage());
        }
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = {"--", "-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("Argument for option A should be equal to 1", "1", cmd.getArgList().get(0));
    }
}