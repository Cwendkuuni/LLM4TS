package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser
 *
 * @author Expert1
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}
     */
    @Test
    public void testParse() {
        // Given
        String[] args = new String[]{"--input", "input.txt", "-o", "output.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file")
                .longOpt("input")
                .requiredOpt(true)
                .argName("file")
                .desc("Input file");
        options.addRequiredOption("o", "output", true, "Output file")
                .longOpt("output")
                .requiredOpt(true)
                .argName("file")
                .desc("Output file");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            Assert.fail("Parse error: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} with optional argument
     */
    @Test
    public void testParseOptionalArgument() {
        // Given
        String[] args = new String[]{"--input", "input.txt", "--output"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file")
                .longOpt("input")
                .requiredOpt(true)
                .argName("file")
                .desc("Input file");
        options.addOption("o", "output", true, "Output file")
                .longOpt("output")
                .argName("file")
                .desc("Output file");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            Assert.fail("Parse error: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} with missing required argument
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredArgument() {
        // Given
        String[] args = new String[]{"--input"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file")
                .longOpt("input")
                .requiredOpt(true)
                .argName("file")
                .desc("Input file");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            throw new MissingArgumentException("Missing argument for option:"
                    + options.getOption("input").getKey());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} with missing option
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseMissingOption() {
        // Given
        String[] args = new String[]{"--unknown"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            if (e.getMessage().contains("Unrecognized option: unknown")) {
                throw e;
            }
            Assert.fail("Parse error: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties)}
     */
    @Test
    public void testParseWithProperties() {
        // Given
        String[] args = new String[]{"--input", "input.txt", "--output", "output.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file")
                .longOpt("input")
                .requiredOpt(true)
                .argName("file")
                .desc("Input file");
        options.addRequiredOption("o", "output", true, "Output file")
                .longOpt("output")
                .requiredOpt(true)
                .argName("file")
                .desc("Output file");
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");

        // When
        CommandLine cmd;
        try {
            cmd = new Parser()
                    .parse(options, args, properties, false);
        } catch (ParseException e) {
            Assert.fail("Parse error: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} with mixed long and short options
     */
    @Test
    public void testParseMixedOptions() {
        // Given
        String[] args = new String[]{"-i", "input.txt", "--output", "output.txt"};
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file")
                .longOpt("input")
                .requiredOpt(true)
                .argName("file")
                .desc("Input file")
                .alt("I");
        options.addRequiredOption("o", "output", true, "Output file")
                .longOpt("output")
                .requiredOpt(true)
                .argName("file")
                .desc("Output file");

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            Assert.fail("Parse error: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} with options in a group
     */
    @Test
    public void testParseOptionsGroup() {
        // Given
        String[] args = new String[]{"-a", "value1", "-b", "value2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Options group", 1);
        group.setRequired(true);
        options.addOptionGroup(group);
        group.addOption(new Option("a", "a", true, "Option A")
                .longOpt("a")
                .desc("Option A")
                .required(true)
                .argName("value"));
        group.addOption(new Option("b", "b", true, "Option B")
                .longOpt("b")
                .desc("Option B")
                .required(true)
                .argName("value"));

        CommandLineParser parser = new DefaultParser();

        // When
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            Assert.fail("Parse error: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals(args.length, cmd.getArgs().size());
        Assert.assertEquals(1, group.getOptions().size());
        Assert.assertTrue(group.isSelected());
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b"));
    }
}