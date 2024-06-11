import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"-a"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        options.addOption(optionA);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testOptionWithValue() {
        String[] args = {"-a", "value"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", true, "This is an option A with value");
        options.addOption(optionA);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testMultipleOptions() {
        String[] args = {"-a", "-b", "valueB"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        Option optionB = new Option("b", "option-b", true, "This is an option B with value");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("c"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        String[] args = {"-c"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        Option optionB = new Option("b", "option-b", true, "This is an option B with value");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }

        Assert.assertFalse(cmd.hasOption("c"));
        Assert.fail("Parsing should have failed.");
    }

    @Test
    public void testUnrecognizedOption() {
        String[] args = {"-x", "valueX"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        Option optionB = new Option("b", "option-b", true, "This is an option B with value");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        List<String> unrecognized = cmd.getArgList();
        Assert.assertEquals(1, unrecognized.size());
        Assert.assertEquals("-x", unrecognized.get(0));
    }

    @Test
    public void testEmptyArgs() {
        String[] args = {};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        Option optionB = new Option("b", "option-b", true, "This is an option B with value");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        List<String> argsList = cmd.getArgList();
        Assert.assertEquals(0, argsList.size());
    }

    @Test
    public void testArgsWithOptions() {
        String[] args = {"arg1", "-a", "arg3"};

        Options options = new Options();
        Option optionA = new Option("a", "option-a", false, "This is an option A");
        options.addOption(optionA);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail("Parsing failed.");
        }

        List<String> argsList = cmd.getArgList();
        Assert.assertEquals(2, argsList.size());
        Assert.assertEquals("arg1", argsList.get(0));
        Assert.assertEquals("arg3", argsList.get(1));
        Assert.assertTrue(cmd.hasOption("a"));
    }
}