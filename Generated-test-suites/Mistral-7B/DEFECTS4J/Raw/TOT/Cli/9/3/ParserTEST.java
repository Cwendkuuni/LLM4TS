package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link Parser} class
 */
public class ParserTest {

    /**
     * Tests parsing of basic options and arguments
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testBasicOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", 1, "b option with value");
        options.addOption("c", 3, "c option with value");
        options.addOption("d", null, "d option without value");

        CommandLineParser parser = new DefaultParser();

        String[] arguments = {"--a", "true", "--b", "2", "--c", "5", "--d"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a"));
        Assert.assertEquals(2, cmd.getOptionValue("b").intValue());
        Assert.assertEquals(5, cmd.getOptionValue("c").intValue());
        Assert.assertNull(cmd.getOptionValue("d"));
        Assert.assertFalse(cmd.hasOption("e"));
        Assert.assertNull(cmd.getOptionValue("e"));

        Assert.assertEquals(Arrays.asList("--a", "--b=2", "--c=5", "--d"),
                cmd.getArgs());
    }

    /**
     * Tests parsing of optional options with arguments
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testOptionalOptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", 1, true, "a option with value");
        options.addOption("b", 2, false, "b option without value");

        String[] arguments = {"--a=1", "--a=2", "--b"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));

        Assert.assertEquals(Arrays.asList(), cmd.getArgs());
    }

    /**
     * Tests parsing of required options
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", true, "b option");

        String[] arguments = {"--a"};
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option 'b'", e.getMessage());
            return;
        }

        Assert.fail("Expected MissingOptionException but got nothing");
    }

    /**
     * Tests parsing of options with long names
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testLongOptionNames() throws ParseException {
        Options options = new Options();
        options.addOption("--long-option-a", true, "long option A");
        options.addOption("--long-option-b", 2, "long option B with value");

        String[] arguments = {"--long-option-a", "true", "--long-option-b=3"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("--long-option-a"));
        Assert.assertTrue(cmd.getOptionValue("--long-option-a"));
        Assert.assertEquals(3, cmd.getOptionValue("--long-option-b").intValue());
    }

    /**
     * Tests parsing of options with short names
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testShortOptionNames() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", 2, "b option with value");

        String[] arguments = {"-a", "--b=2", "--"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a"));
        Assert.assertEquals(2, cmd.getOptionValue("b").intValue());
        Assert.assertFalse(cmd.hasOptions());
        Assert.assertEquals(Arrays.asList(), cmd.getArgs());
    }

    /**
     * Tests parsing of options with values
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testOptionsWithValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", 2, "b option with value");

        String[] arguments = {"--a", "--b=test"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a"));
        Assert.assertEquals("test", cmd.getOptionValue("b"));
    }

    /**
     * Tests parsing of options with no values
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testOptionsWithNoValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("b", true, "b option");

        String[] arguments = {"--a", "--b"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertFalse(cmd.getOptionValue("a") instanceof String);
        Assert.assertFalse(cmd.getOptionValue("b") instanceof String);
        Assert.assertEquals(Arrays.asList(), cmd.getArgs());
    }

    /**
     * Tests parsing of options with both long and short names
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testOptionsWithBothLongShortNames() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");
        options.addOption("a", 2, "a option with value");

        String[] arguments = {"--a", "true", "--a=3"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.getOptionValue("a").equals("true"));
        Assert.assertEquals(3, cmd.getOptionValue("a", Integer.class).intValue());
    }

    /**
     * Tests parsing of options in groups
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testOptionsInGroups() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("mygroup", "My group");
        group.setRequired(true);

        group.addOption("a", true, "a option in group");
        group.addOption("b", true, "b option in group");

        options.addOptionGroup(group);
        options.addOption("c", true, "c option not in group");

        String[] arguments = {"--mygroup.a", "--mygroup.b", "--c"};
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, arguments);
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option 'a'", e.getMessage());
            return;
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("c"));

        Assert.assertEquals(Arrays.asList(), cmd.getArgs());
    }

    /**
     * Tests parsing of invalid options
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testInvalidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");

        String[] arguments = {"--e", "--a"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
    }

    /**
     * Tests parsing of unrecognized options with double-dash
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptionsWithDoubleDash() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");

        String[] arguments = {"--", "--a"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
    }

    /**
     * Tests parsing of unrecognized options without double-dash
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptionsWithoutDoubleDash() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");

        String[] arguments = {"--e", "--a"};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);
    }

    /**
     * Tests parsing of empty arguments list
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testEmptyArgsList() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "a option");

        String[] arguments = {};
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertFalse(cmd.hasOptions());
        Assert.assertEquals(Arrays.asList(), cmd.getArgs());
    }
}