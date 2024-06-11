package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        // Test case for parsing valid arguments
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        CommandLineParser parser = new DefaultParser();
        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs());

        // Test case for parsing arguments with optional arguments
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("t", "type", false, "File type");

        arguments = new String[]{"-i", "input.txt", "-o", "output.txt", "-t", "txt"};
        cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("txt", cmd.getOptionValue("type"));
        Assert.assertNull(cmd.getArgs());

        // Test case for parsing arguments with arguments
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("t", "type", false, "File type");
        options.addOption("f", "file", true, "File name");

        arguments = new String[]{"-i", "input.txt", "-o", "output.txt", "-t", "txt", "-f", "file.txt"};
        cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("txt", cmd.getOptionValue("type"));
        Assert.assertEquals("file.txt", cmd.getArgs()[0]);

        // Test case for parsing arguments with properties
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");

        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("t", "type", false, "File type");

        arguments = new String[0];
        cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("type"));
        Assert.assertNull(cmd.getArgs());

        // Test case for parsing arguments with an invalid option
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        arguments = new String[]{"-i", "input.txt", "-x", "output.txt"};
        try {
            cmd = parser.parse(options, arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }

        // Test case for parsing arguments with missing required options
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        arguments = new String[]{"-o", "output.txt"};
        try {
            cmd = parser.parse(options, arguments, false);
            Assert.fail("Expected MissingOptionException");
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: input", e.getMessage());
        }

        // Test case for parsing arguments with an incorrect number of arguments
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");
        options.addOption("t", "type", false, "File type");

        arguments = new String[]{"-i", "input.txt", "-o", "output.txt", "arg"};
        try {
            cmd = parser.parse(options, arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized value 'arg' for option: arg", e.getMessage());
        }

        // Test case for parsing arguments with an empty argument list
        options = new Options();
        options.addOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        arguments = new String[0];
        try {
            cmd = parser.parse(options, arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("No argument value for option: input", e.getMessage());
        }
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        // Test case for parsing arguments with an OptionGroup
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1");
        group.setRequired(false);
        group.addOption(new Option("i", "input", true, "Input file path"));
        group.addOption(new Option("o", "output", true, "Output file path"));
        options.addOptionGroup(group);
        options.addOption("t", "type", false, "File type");

        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = new DefaultParser().parse(options, arguments, false);

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof OptionGroup);
        Assert.assertEquals("group1", cmd.getOptions().get(0).getLongOpt());
        Assert.assertFalse(cmd.getOptions().get(0).isRequired());
        Assert.assertEquals("input.txt", cmd.getOptions().get(1).getLongOpt());
        Assert.assertEquals(true, cmd.getOptions().get(1).isRequired());
        Assert.assertEquals("Input file path", cmd.getOptions().get(1).getDescription());
        Assert.assertNull(cmd.getArgs());

        arguments = new String[]{"--group1", "input.txt", "-o", "output.txt"};
        cmd = new DefaultParser().parse(options, arguments, false);

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof OptionGroup);
        Assert.assertEquals("group1", cmd.getOptions().get(0).getLongOpt());
        Assert.assertFalse(cmd.getOptions().get(0).isRequired());
        Assert.assertEquals("input.txt", cmd.getOptions().get(1).getLongOpt());
        Assert.assertNull(cmd.getArgs());

        arguments = new String[]{"-t", "txt"};
        try {
            cmd = new DefaultParser().parse(options, arguments, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option: input", e.getMessage());
        }

        arguments = new String[]{"-t", "txt", "input.txt"};
        cmd = new DefaultParser().parse(options, arguments, false);

        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof OptionGroup);
        Assert.assertEquals("group1", cmd.getOptions().get(0).getLongOpt());
        Assert.assertFalse(cmd.getOptions().get(0).isRequired());
        Assert.assertEquals("input.txt", cmd.getOptions().get(1).getLongOpt());
        Assert.assertFalse(cmd.getOptions().get(1).isRequired());
        Assert.assertEquals("Input file path", cmd.getOptions().get(1).getDescription());
        Assert.assertEquals("txt", cmd.getOptions().get(2).getLongOpt());
        Assert.assertFalse(cmd.getOptions().get(2).isRequired());
        Assert.assertEquals("File type", cmd.getOptions().get(2).getDescription());
        Assert.assertNull(cmd.getArgs());
    }
}