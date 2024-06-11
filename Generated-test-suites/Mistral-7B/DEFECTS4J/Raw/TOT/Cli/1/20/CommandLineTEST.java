import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "-b", "2", "-c", "3"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value for an option");
        options.addOption("b", "anotherOption", true, "A value for another option");
        options.addOption("c", "yetAnotherOption", true, "A value for yet another option");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));

        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));

        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }

    @Test
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"-x", "1", "-b", "2", "-c", "3"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value for an option");
        options.addOption("b", "anotherOption", true, "A value for another option");
        options.addOption("c", "yetAnotherOption", true, "A value for yet another option");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertFalse(cmd.hasOption("x"));
        Assert.assertEquals(null, cmd.getOptionValue("x"));

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(1, remainingArgs.size());
        Assert.assertEquals("1", remainingArgs.get(0));
    }

    @Test
    public void testMissingValueOption() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value for an option");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(null, cmd.getOptionValue("a"));

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        new CommandLineParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testMultipleInvalidOptions() throws ParseException {
        String[] args = new String[]{"--invalid1", "--invalid2"};
        Options options = new Options();

        new CommandLineParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value for an option").required(true);

        try {
            new CommandLineParser().parse(options, args);
        } catch (ParseException e) {
            Assert.assertEquals("The following required options do not have values: [a]", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = new String[]{"--longOption", "value1", "--anotherLongOption", "value2"};
        Options options = new Options();
        Option longOption = new Option("l", "longOption", true, "A value for a long option");
        Option anotherLongOption = new Option("another", "anotherLongOption", true, "A value for another long option");

        options.addOption(longOption);
        options.addOption(anotherLongOption);

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("longOption"));
        Assert.assertEquals("value1", cmd.getOptionValue("longOption"));

        Assert.assertTrue(cmd.hasOption("anotherLongOption"));
        Assert.assertEquals("value2", cmd.getOptionValue("anotherLongOption"));

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }

    @Test
    public void testMixedShortAndLongOptions() throws ParseException {
        String[] args = new String[]{"-a", "--anotherOption", "value1", "-b", "--anotherOption", "value2"};
        Options options = new Options();
        Option shortOption = new Option("a", "shortOption", true, "A value for a short option");
        Option longOption = new Option("another", "anotherOption", true, "A value for a long option");

        options.addOption(shortOption);
        options.addOption(longOption);

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("", cmd.getOptionValue("a"));

        Assert.assertTrue(cmd.hasOption("another"));
        Assert.assertEquals("value1", cmd.getOptionValue("another"));

        Assert.assertTrue(cmd.hasOption("another"));
        Assert.assertEquals("value2", cmd.getOptionValue("another"));

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }

    @Test
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unrecognizedOption", "value1", "--anotherOption", "value2"};
        Options options = new Options();
        Option longOption = new Option("another", "anotherOption", true, "A value for a long option");

        options.addOption(longOption);

        CommandLine cmd = new CommandLineParser().parse(options, args);

        List<String> remainingArgs = cmd.getArgList();

        Assert.assertEquals(1, remainingArgs.size());
        Assert.assertEquals("--unrecognizedOption", remainingArgs.get(0));
    }

    @Test
    public void testUnrecognizedOptionValue() throws ParseException {
        String[] args = new String[]{"--unrecognizedOption", "value1", "--anotherOption", "value2", "--unrecognizedOption=value3"};
        Options options = new Options();
        Option longOption = new Option("another", "anotherOption", true, "A value for a long option");

        options.addOption(longOption);

        CommandLine cmd = new CommandLineParser().parse(options, args);

        List<String> remainingArgs = cmd.getArgList();

        Assert.assertEquals(1, remainingArgs.size());
        Assert.assertEquals("--unrecognizedOption=value3", remainingArgs.get(0));
    }

    @Test
    public void testEmptyArgs() throws ParseException {
        String[] args = new String[0];
        Options options = new Options();

        CommandLine cmd = new CommandLineParser().parse(options, args);

        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }

    @Test
    public void testEmptyOptions() throws ParseException {
        String[] args = new String[]{"--", "-"};
        Options options = new Options();

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertEquals(0, cmd.getOptions().length);
        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(2, remainingArgs.size());
        Assert.assertEquals("--", remainingArgs.get(0));
        Assert.assertEquals("-", remainingArgs.get(1));
    }

    @Test
    public void testNoOptions() throws ParseException {
        String[] args = new String[0];
        Options options = new Options();

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertEquals(0, cmd.getOptions().length);
        List<String> remainingArgs = cmd.getArgList();
        Assert.assertEquals(0, remainingArgs.size());
    }
}