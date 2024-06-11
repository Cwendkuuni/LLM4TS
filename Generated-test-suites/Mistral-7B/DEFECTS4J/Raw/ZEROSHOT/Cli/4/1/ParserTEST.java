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
 * Test class for Parser.
 */
public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        String[] args = {"-a", "1", "--b", "2", "--c=3", "-d", "4", "--e", "5"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", "2", true, "Option B");
        options.addOption("c", "3", true, "Option C");
        options.addOption("d", false, "Option D");
        options.addOption("e", true, "Option E");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"1", "2", "3"}, cmd.getOptionValues("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertFalse(cmd.hasOption("b")); // value is present, but option itself is not
        Assert.assertFalse(cmd.hasOption("f"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertNull(cmd.getOptionValue("f"));
        Assert.assertNull(cmd.getOptionValue("g"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"-a", "1", "--c=3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", "3", true, "Option C");
        options.addOption("d", true, "Required Option D");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // This should throw an exception
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"-f", "1", "--a", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // This should throw an exception
    }

    @Test
    public void testParseProperties() throws ParseException {
        String[] args = {"--foo=bar", "--quux=quux"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        properties.setProperty("quux", "quux");

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("quux", true, "Option Quux");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("bar", cmd.getOptionValue("foo"));
        Assert.assertEquals("quux", cmd.getOptionValue("quux"));
    }
}