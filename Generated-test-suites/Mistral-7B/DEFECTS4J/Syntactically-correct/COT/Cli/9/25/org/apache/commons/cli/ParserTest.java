package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    private Parser parser;

    @Before
    public void setUp() {
        parser = new DefaultParser();
    }

    @Test
    public void testSetOptions() throws ParseException {
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        parser.setOptions(options);
        Assert.assertEquals(options, parser.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        parser.setOptions(options);
        List<Option> requiredOptions = parser.getRequiredOptions();
        Assert.assertEquals(1, requiredOptions.size());
        Assert.assertEquals("o", requiredOptions.get(0).getKey());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"arg1", "--opt", "value", "--otherOpt", "arg2"};
        List<String> result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList("arg1", "--opt", "value", "--otherOpt", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result.toArray(new String[0]));
    }

    @Test
    public void testParse() throws ParseException {
        String[] arguments = {"arg1", "--opt", "value", "--otherOpt"};
        Options options = new Options();
        options.addOption("opt", true, "This is an option");

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(arguments.length, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("opt"));
        Assert.assertFalse(cmd.hasOption("unknown"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = {"arg1"};
        Options options = new Options();
        options.addRequiredOption("o", "option", false, "This is an option");
        parser.setOptions(options);
        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] arguments = {"arg1", "--unknown"};
        Options options = new Options();
        parser.setOptions(options);
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = {"arg1", "--opt=value"};
        Properties properties = new Properties();
        properties.setProperty("opt", "valueFromProperties");

        Options options = new Options();
        options.addOption("opt", true, "This is an option");

        CommandLine cmd = parser.parse(options, arguments, properties, false);
        Assert.assertEquals(arguments.length, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("opt"));
        Assert.assertEquals("valueFromProperties", cmd.getOptionValue("opt"));
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        String[] arguments = {"arg1", "--opt=value"};
        Options options = new Options();
        options.addOption("opt", true, "This is an option", "value");

        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(arguments.length, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("opt"));
        Assert.assertEquals("value", cmd.getOptionValue("opt"));
    }
}