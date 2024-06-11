package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        String[] args = new String[]{"--input", "input.txt", "--output", "output.txt", "-o", "output2.txt"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");
        options.addOption("output", true, "o", "output file");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(args.length, cmd.getArgs().size());
        for (int i = 0; i < args.length; i++) {
            Assert.assertEquals(args[i], cmd.getArgs().get(i));
        }

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("output2.txt", cmd.getOptionValue("o"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"--input", "input.txt"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");
        options.addOption("output", true, "o", "output file");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknown", "input.txt"};

        Options options = new Options();
        options.addOption("input", true, "i", "input file");
        options.addOption("output", true, "o", "output file");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = new String[]{"--input", "input.txt", "--output"};

        Options options = new Options();
        Option outputOption = new Option("output", true, "o", "output file")
                .required(true);
        options.addOption(outputOption);
        options.addOption("input", true, "i", "input file");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        String[] args = new String[]{"--input", "input.txt"};

        Options options = new Options();
        Option inputOption = new Option("input", true, "i", "input file")
                .required(true);
        Option outputOption = new Option("output", true, "o", "output file")
                .required(true);
        options.addOption(inputOption);
        options.addOption(outputOption);

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, true);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--input", "input.txt"};

        Properties props = new Properties();
        props.setProperty("input", "input2.txt");

        Options options = new Options();
        Option inputOption = new Option("input", true, "i", "input file")
                .required(true);
        options.addOption(inputOption);
        options.addDefaults(true);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, true);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("input2.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithPropertiesAndArguments() throws ParseException {
        String[] args = new String[]{"--input", "input.txt", "--output", "output.txt"};

        Properties props = new Properties();
        props.setProperty("input", "input2.txt");
        props.setProperty("output", "output2.txt");

        Options options = new Options();
        Option inputOption = new Option("input", true, "i", "input file")
                .required(false);
        Option outputOption = new Option("output", true, "o", "output file")
                .required(false);
        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addDefaults(true);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, true);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("input2.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output2.txt", cmd.getOptionValue("output"));
    }
}