package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for Parser class.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        options = new Options();
    }

    @Test
    public void testParseWithNoArguments() throws ParseException
    {
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleArgument() throws ParseException
    {
        String[] args = {"arg"};
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException
    {
        String[] args = {"-o"};
        options.addOption("o", "option", false, "An option");
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("o"));
        assertFalse(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSingleOptionValue() throws ParseException
    {
        String[] args = {"-o", "value"};
        options.addOption("o", "option", true, "An option with value");
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals("value", cmd.getOptionValue("o"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException
    {
        String[] args = {"-o1", "-o2"};
        options.addOption("o1", "option1", false, "Option1");
        options.addOption("o2", "option2", false, "Option2");
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("o1"));
        assertTrue(cmd.hasOption("o2"));
        assertFalse(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithMultipleOptionsAndValues() throws ParseException
    {
        String[] args = {"-o1", "val1", "-o2", "val2"};
        options.addOption("o1", "option1", true, "Option1 with value");
        options.addOption("o2", "option2", true, "Option2 with value");
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals("val1", cmd.getOptionValue("o1"));
        assertEquals("val2", cmd.getOptionValue("o2"));
    }

    @Test
    public void testParseWithRequiredOptionMissing() throws ParseException, MissingOptionException
    {
        String[] args = {};
        options.addOption("r", "requiredOption", true, "A required option");
        options.getOption("r").setRequired(true);
        CommandLine cmd = assertThrows(MissingOptionException.class, () -> parser.parse(options, args, true));
        List<String> expected = Arrays.asList("requiredOption");
        assertEquals(expected, cmd.getMissingOptions());
    }

    @Test
    public void testParseWithRequiredOptionPresent() throws ParseException
    {
        String[] args = {"-r", "value"};
        options.addOption("r", "requiredOption", true, "A required option");
        CommandLine cmd = parser.parse(options, args, true);
        assertTrue(cmd.hasOption("r"));
        assertEquals("value", cmd.getOptionValue("r"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException, UnrecognizedOptionException
    {
        String[] args = {"-i", "value"};
        options.addOption("o", "option", false, "An option");
        CommandLine cmd = assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, true));
        assertEquals("-i", cmd.getParsed().getOption("i").getName());
        assertEquals("value", cmd.getParsed().getOption("i").getValue());
    }

    @Test
    public void testParseWithMultipleInvalidOptions() throws ParseException, UnrecognizedOptionException
    {
        String[] args = {"-i1", "value1", "-i2", "value2"};
        options.addOption("o", "option", false, "An option");
        CommandLine cmd = assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, true));
        List<String> expected = Arrays.asList("-i1", "-i2");
        assertEquals(expected, cmd.getParsed().getUnrecognized());
    }

    @Test
    public void testParseWithInvalidOptionValue() throws ParseException, ParseException, MissingArgumentException
    {
        String[] args = {"-a", "value", "--", "arg"};
        options.addOption("a", "argOption", true, "An option with arg");
        CommandLine cmd = assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, true));
        assertEquals("arg", cmd.getParsed().getOption("a").getValue());
        assertEquals(Arrays.asList("arg"), cmd.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException, ParseException, MissingArgumentException
    {
        String[] args = {"--", "arg"};
        options.addOption("a", "argOption", true, "An option with arg");
        CommandLine cmd = parser.parse(options, args, true);
        assertNull(cmd.getOptionValue("a"));
        assertEquals(Arrays.asList("arg"), cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"-p", "property1=value1,property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        CommandLine cmd = parser.parse(options, args, properties, true);
        assertEquals(Arrays.asList(), cmd.getArgs());
        assertEquals(properties, cmd.getParsed().getProperties());
    }

    @Test
    public void testParseWithPropertiesAndOptions() throws ParseException
    {
        String[] args = {"-p", "property1=value1,property2=value2", "-o", "option"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        options.addOption("o", "option", false, "An option");
        CommandLine cmd = parser.parse(options, args, properties, true);
        assertTrue(cmd.hasOption("o"));
        assertEquals(Arrays.asList(), cmd.getArgs());
        assertEquals(properties, cmd.getParsed().getProperties());
    }
}