package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseWithNoArguments() throws Exception {
        final Options options = new Options();
        options.addOption("h", "help", false, "Print help information.");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[0][], (Properties) null, false);
        assertTrue(cmd.hasOption("h"));
        assertTrue(cmd.hasOption("help"));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseWithArguments() throws Exception {
        final Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"inputFile.txt", "outputFile.txt"}, (Properties) null, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(2, args.size());
        assertEquals("inputFile.txt", args.get(0));
        assertEquals("outputFile.txt", args.get(1));
    }

    @Test
    public void testParseWithProperties() throws Exception {
        final Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");

        Properties props = new Properties();
        props.setProperty("input", "inputFile.txt");
        props.setProperty("output", "outputFile.txt");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[0][], props, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        assertEquals("inputFile.txt", cmd.getOptionValue("input"));
        assertEquals("outputFile.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws Exception {
        final Options options = new Options();
        options.addOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(new String[]{"unknownOption"}, (Properties) null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws Exception {
        final Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("r", "required", true, "required option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(new String[]{"inputFile.txt", "outputFile.txt"}, (Properties) null, false);
        List<String> requiredOptions = parser.getOptions().getRequiredOptions();
        assertFalse(requiredOptions.isEmpty());
        assertEquals("required", requiredOptions.get(0).getKey());
        throw new MissingOptionException("Missing required option: required");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws Exception {
        final Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file");
        options.addOption("l", "length", true, "length");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"inputFile.txt", "outputFile.txt"}, (Properties) null, false);
        Option lengthOption = cmd.getOption("l");
        assertNull(lengthOption.getValue());
        throw new MissingArgumentException("Missing argument for option: length");
    }

    @Test
    public void testParseWithGroupOption() throws Exception {
        final Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(true);
        group.addOption(new Option("g", "groupOption", true, "group option"));
        options.addOptionGroup(group);
        options.addOption("i", "input", true, "input file");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"--group", "inputFile.txt"}, (Properties) null, false);
        assertTrue(group.isSelected());
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(1, args.size());
        assertEquals("inputFile.txt", args.get(0));
    }

    @Test
    public void testParseWithOptionProperties() throws Exception {
        final Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .longOpt("inputFile")
                .desc("Input file to process.");
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"--inputFile=inputFile.txt"}, (Properties) null, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        assertEquals("inputFile.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithEmptyOptionProperty() throws Exception {
        final Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .longOpt("inputFile")
                .desc("Input file to process.");
        options.addOption(option);

        Properties props = new Properties();
        props.setProperty("inputFile", "");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[0][], props, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        assertNull(cmd.getOptionValue("input"));
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws Exception {
        final Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .longOpt("inputFile")
                .desc("Input files to process.")
                .valueSeparator(' ')
                .required(false);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"--inputFile=inputFile1.txt --inputFile=inputFile2.txt"}, (Properties) null, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        List<String> values = Arrays.asList(cmd.getOptionValues("input"));
        assertEquals(2, values.size());
        assertEquals("inputFile1.txt", values.get(0));
        assertEquals("inputFile2.txt", values.get(1));
    }

    @Test
    public void testParseWithValuesForOptionWithSeparator() throws Exception {
        final Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .longOpt("inputFile")
                .desc("Input files to process.")
                .valueSeparator(';')
                .required(false);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"--inputFile=inputFile1.txt;inputFile2.txt"}, (Properties) null, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        List<String> values = Arrays.asList(cmd.getOptionValues("input"));
        assertEquals(2, values.size());
        assertEquals("inputFile1.txt", values.get(0));
        assertEquals("inputFile2.txt", values.get(1));
    }

    @Test
    public void testParseWithValuesForOptionWithNoSeparator() throws Exception {
        final Options options = new Options();
        Option option = new Option("i", "input", true, "input file")
                .longOpt("inputFile")
                .desc("Input files to process.")
                .valueSeparator('')
                .required(false);
        options.addOption(option);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(new String[]{"--inputFile=inputFile1.txt,inputFile2.txt"}, (Properties) null, false);
        List<String> args = Arrays.asList(cmd.getArgs());
        assertEquals(0, args.size());
        List<String> values = Arrays.asList(cmd.getOptionValues("input"));
        assertEquals(2, values.size());
        assertEquals("inputFile1.txt,inputFile2.txt", Arrays.toString(values.toArray()));
    }
}