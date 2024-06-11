package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParsingException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.test.util.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test the functionality of the {@link Parser} class.
 */
public class ParserTest
{
    /**
     * Test parsing of commandline arguments using {@link Parser#parse}.
     */
    @Test
    public void testParse()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        String[] expectedArgs = new String[]{"input.txt", "output.txt"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertFalse(cmd.hasOption("unknown"));
    }

    /**
     * Test parsing of commandline arguments using {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithProperties()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);

        Properties properties = new Properties();
        properties.put("input", "input.txt");
        properties.put("output", "output.txt");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        String[] expectedArgs = new String[]{"input.txt", "output.txt"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertFalse(cmd.hasOption("unknown"));
    }

    /**
     * Test parsing of commandline arguments with unrecognized options using {@link Parser#parse}.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--unknown"};
        CommandLine cmd = parser.parse(options, arguments);
    }

    /**
     * Test parsing of commandline arguments with missing options using {@link Parser#parse}.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingOption()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option fileOption = new Option("f", "file", true, "file path");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(fileOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(fileOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);

        throw new AssertionError("Expected MissingOptionException not thrown.");
    }

    /**
     * Test parsing of commandline arguments with missing arguments using {@link Parser#parse}.
     */
    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option fileOption = new Option("f", "file", true, "file path");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(fileOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(fileOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        String[] arguments = new String[]{"--input", "--output"};
        CommandLine cmd = parser.parse(options, arguments);

        throw new AssertionError("Expected MissingArgumentException not thrown.");
    }

    /**
     * Test parsing of commandline arguments with mixed options and values using {@link Parser#parse}.
     */
    @Test
    public void testParseWithMixedOptionsAndValues()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option verboseOption = new Option("v", "verbose", false, "Prints verbose output.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(verboseOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        String[] arguments = new String[]{"--input", "input.txt", "--output", "output.txt", "--verbose"};
        CommandLine cmd = parser.parse(options, arguments);

        String[] expectedArgs = new String[]{"input.txt", "output.txt"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertTrue(cmd.hasOption(verboseOption.getKey()));
    }

    /**
     * Test parsing of commandline arguments with mixed options and values using {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithMixedOptionsAndValuesWithProperties()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option verboseOption = new Option("v", "verbose", false, "Prints verbose output.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(verboseOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        Properties properties = new Properties();
        properties.put("input", "input.txt");
        properties.put("output", "output.txt");

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        String[] expectedArgs = new String[]{"input.txt", "output.txt"};
        assertArrayEquals(expectedArgs, cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertTrue(cmd.hasOption(verboseOption.getKey()));
    }

    /**
     * Test parsing of commandline arguments with mixed options and missing values using {@link Parser#parse}.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMixedOptionsAndMissingValues()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option verboseOption = new Option("v", "verbose", false, "Prints verbose output.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(verboseOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        String[] arguments = new String[]{"--input", "--output"};
        CommandLine cmd = parser.parse(options, arguments);

        throw new AssertionError("Expected MissingArgumentException not thrown.");
    }

    /**
     * Test parsing of commandline arguments with mixed options and missing values using {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseWithMixedOptionsAndMissingValuesWithProperties()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");
        Option verboseOption = new Option("v", "verbose", false, "Prints verbose output.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);
        options.addOption(verboseOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        Properties properties = new Properties();

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        throw new AssertionError("Expected MissingArgumentException not thrown.");
    }

    /**
     * Test parsing of commandline arguments with empty values using {@link Parser#parse}.
     */
    @Test
    public void testParseWithEmptyValues()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        String[] arguments = new String[]{"--input", "--output"};
        CommandLine cmd = parser.parse(options, arguments);

        // The empty values should be ignored.
        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertFalse(cmd.hasOption("help"));
    }

    /**
     * Test parsing of commandline arguments with empty values using {@link Parser#parse(Options, String[], Properties)}.
     */
    @Test
    public void testParseWithEmptyValuesWithProperties()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "input", true, "input file");
        Option outputOption = new Option("o", "output", true, "output file");
        Option helpOption = new Option("h", "help", false, "Prints this help message.");

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOption(helpOption);

        List<String> requiredOptions = new ArrayList<>();
        requiredOptions.add(inputOption.getKey());
        requiredOptions.add(outputOption.getKey());

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);

        Properties properties = new Properties();

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        // The empty values should be ignored.
        assertArrayEquals(new String[0], cmd.getArgs());
        assertEquals(inputOption, cmd.getOption(inputOption.getKey()));
        assertEquals(outputOption, cmd.getOption(outputOption.getKey()));
        assertFalse(cmd.hasOption("help"));
    }
}