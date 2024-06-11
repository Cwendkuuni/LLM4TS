package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.java
 */
public class ParserTest {

    @Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--b", "2", "--c=3", "--d"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C with argument");
        options.addOption("d", true, "Option D");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));

        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));

        Assert.assertEquals(true, cmd.hasOption("c"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));

        Assert.assertEquals(true, cmd.hasOption("d"));
        Assert.assertEquals("", cmd.getOptionValue("d"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--a", "--b"};

        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", true, "Option B", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = new String[]{"-z", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] arguments = new String[]{"--c"};

        Options options = new Options();
        options.addOption("c", true, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        String[] arguments = new String[]{"--a"};

        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", true, "Option B", true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--config", "config.properties"};

        Properties properties = new Properties();
        properties.setProperty("optionA", "1");
        properties.setProperty("optionB.value", "2");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments, properties);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));

        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(0, args.size());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--a", "1", "-", "3", "-c=4"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("c", true, "Option C with argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(arguments, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));

        Assert.assertEquals(true, cmd.hasOption("c"));
        Assert.assertEquals("4", cmd.getOptionValue("c"));

        List<String> args = cmd.getArgs();
        Assert.assertEquals(1, args.size());
        Assert.assertEquals("3", args.get(0));
    }
}