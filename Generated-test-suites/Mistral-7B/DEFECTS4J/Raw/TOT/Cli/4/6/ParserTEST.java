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
 * Test for {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = new String[]{"--input", "input.txt", "--output", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"--output", "output.txt"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");

        CommandLine cmd = parser.parse(options, args);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknown-option"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");

        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = new String[]{"--group", "group1", "--option1", "value1"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        options.addOptionGroup(group1);
        group1.addOption("o1", "option1", true, "option1");

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertTrue(group1.isSelected());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws ParseException {
        String[] args = new String[]{"--option"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        Option option = new Option("o", "option", true, "option value");
        option.setRequired(false);
        options.addOption(option);

        CommandLine cmd = parser.parse(options, args);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--property1", "value1", "--property2", "value2"};

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addPropertyConstants(new Properties() {{
            setProperty("property1", "value1");
            setProperty("property2", "value2");
        }});

        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("value1", cmd.getOptionValue("property1"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));
    }
}