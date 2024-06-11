package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"--test", "-a", "1", "-b", "2", "-c", "3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("test", true, "test flag");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        CommandLine commandLine = parser.parse(options, args, true);

        Assert.assertEquals(true, commandLine.hasOption("test"));
        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertEquals("2", commandLine.getOptionValue("b"));
        Assert.assertEquals("3", commandLine.getOptionValue("c"));
        Assert.assertEquals(Arrays.asList("--test", "-c", "3"), commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"-a", "1"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("test", true, "test flag");
        options.addOption("a", true, "option a");
        options.addRequiredOption("b", true, "required option");
        parser.parse(options, args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"--not-an-option", "-a", "1"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("test", true, "test flag");
        options.addOption("a", true, "option a");
        parser.parse(options, args, true);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "option a with argument");
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--test", "-p", "prop1=val1", "-p", "prop2=val2"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("test", true, "test flag");
        options.addOption("p", true, "property option");
        CommandLine commandLine = parser.parse(options, args, new Properties());

        Assert.assertEquals(true, commandLine.hasOption("test"));
        Assert.assertEquals(2, commandLine.getNumOptions());
        Assert.assertEquals(Arrays.asList("prop1=val1", "prop2=val2"), commandLine.getOptionValues("p"));
        Assert.assertEquals(Arrays.asList(), commandLine.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = {"--test", "-a", "1", "-b", "2", "-c", "3", "--"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("test", true, "test flag");
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        CommandLine commandLine = parser.parse(options, args, true, true);

        Assert.assertEquals(true, commandLine.hasOption("test"));
        Assert.assertEquals("1", commandLine.getOptionValue("a"));
        Assert.assertEquals("2", commandLine.getOptionValue("b"));
        Assert.assertEquals("3", commandLine.getOptionValue("c"));
        Assert.assertEquals(Arrays.asList("--test"), commandLine.getArgs());
    }
}

class DefaultParser extends Parser {
    // empty implementation
}