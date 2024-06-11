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
 * Test class for the Parser class.
 */
public class ParserTest {

    @Test
    public void testBasicUsage() throws Exception {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception {
        String[] args = {"--test"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testOptionalOption() throws Exception {
        String[] args = {"--test", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", "defaultValue", "Optional Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("3", cmd.getOptionValue("a", "defaultValue"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test
    public void testMultipleValues() throws Exception {
        String[] args = {"--list", "item1", "item2"};
        Options options = new Options();
        options.addOption("list", true, "List of items");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("list"));
        Assert.assertArrayEquals(new String[]{"item1", "item2"},
                cmd.getOptionValues("list"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgumentType() throws Exception {
        String[] args = {"--test", "3", "invalid"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test
    public void testDefaultValues() throws Exception {
        String[] args = {"--test"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "defaultValueB");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(null, cmd.getOptionValue("a"));
        Assert.assertEquals("defaultValueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testNoArguments() throws Exception {
        String[] args = {};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertFalse(cmd.hasOptions());
    }

    @Test
    public void testHelp() throws Exception {
        String[] args = {"--help"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options, true, true);
        System.out.println(help);
    }
}