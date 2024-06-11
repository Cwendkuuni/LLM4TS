package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test cases for the Parser class.
 */
public class ParserTest
{
    /**
     * Test the {@code parse} method with no options and no arguments.
     */
    @Test
    public void testParseNoOptionsNoArguments()
    {
        Options options = new Options();
        String[] arguments = new String[0];
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(0, parsedCmd.getOptions().size());
    }

    /**
     * Test the {@code parse} method with no options and one argument.
     */
    @Test
    public void testParseOneArgument()
    {
        Options options = new Options();
        String[] arguments = {"arg1"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(1, parsedCmd.getArgs().size());
        assertEquals("arg1", parsedCmd.getArgs().get(0));
        assertEquals(0, parsedCmd.getOptions().size());
    }

    /**
     * Test the {@code parse} method with an option without value.
     */
    @Test
    public void testParseOptionWithoutValue()
    {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);
        String[] arguments = {"--option"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(1, parsedCmd.getOptions().size());
        Option optionInCmd = parsedCmd.getOption("option");
        assertNotNull(optionInCmd);
        assertFalse(optionInCmd.getValue().isPresent());
    }

    /**
     * Test the {@code parse} method with an option with value.
     */
    @Test
    public void testParseOptionWithValue()
    {
        Options options = new Options();
        Option option = new Option("o", "option", true, "an option with value");
        options.addOption(option);
        String[] arguments = {"--option=value"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(1, parsedCmd.getOptions().size());
        Option optionInCmd = parsedCmd.getOption("option");
        assertNotNull(optionInCmd);
        assertEquals("value", optionInCmd.getValue().get());
    }

    /**
     * Test the {@code parse} method with multiple options.
     */
    @Test
    public void testParseMultipleOptions()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", false, "option1");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--option1", "--option2=value"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(2, parsedCmd.getOptions().size());
        Option option1InCmd = parsedCmd.getOption("option1");
        assertNotNull(option1InCmd);
        assertFalse(option1InCmd.getValue().isPresent());
        Option option2InCmd = parsedCmd.getOption("option2");
        assertNotNull(option2InCmd);
        assertEquals("value", option2InCmd.getValue().get());
    }

    /**
     * Test the {@code parse} method with an unrecognized option.
     */
    @Test
    public void testParseUnrecognizedOption()
    {
        Options options = new Options();
        String[] arguments = {"--unrecognized"};
        assertThrows(UnrecognizedOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }

    /**
     * Test the {@code parse} method with required options.
     */
    @Test
    public void testParseRequiredOptions()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 with value");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--option2"};
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }

    /**
     * Test the {@code parse} method with optional options.
     */
    @Test
    public void testParseOptionalOptions()
    {
        Options options = new Options();
        Option option = new Option("o", "option", false, "an option");
        options.addOption(option);
        String[] arguments = {"--option", "--option", "--"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(2, parsedCmd.getOptions().size());
        Option optionInCmd = parsedCmd.getOption("option");
        assertNotNull(optionInCmd);
        assertTrue(optionInCmd.isSet());
    }

    /**
     * Test the {@code parse} method with long and short option names.
     */
    @Test
    public void testParseLongAndShortOptions()
    {
        Options options = new Options();
        Option longOption = new Option("longOption", "long", false, "long option");
        Option shortOption = new Option("s", "short", false, "short option");
        options.addOption(longOption);
        options.addOption(shortOption);
        String[] arguments = {"--longOption", "--short"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(2, parsedCmd.getOptions().size());
        Option longOptionInCmd = parsedCmd.getOption("longOption");
        assertNotNull(longOptionInCmd);
        assertTrue(longOptionInCmd.isSet());
        Option shortOptionInCmd = parsedCmd.getOption("short");
        assertNotNull(shortOptionInCmd);
        assertTrue(shortOptionInCmd.isSet());
    }

    /**
     * Test the {@code parse} method with an option group.
     */
    @Test
    public void testParseOptionGroup()
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        Option option1 = new Option("g1", "option1", false, "option group option1");
        Option option2 = new Option("g2", "option2", false, "option group option2");
        group.addOption(option1);
        group.addOption(option2);
        options.addOptionGroup(group);
        String[] arguments = {"--group", "--g1"};
        CommandLine parsedCmd = new DefaultParser().parse(options, arguments);
        assertNotNull(parsedCmd);
        assertEquals(0, parsedCmd.getArgs().size());
        assertEquals(2, parsedCmd.getOptions().size());
        Option option1InCmd = parsedCmd.getOption("option1");
        assertNotNull(option1InCmd);
        assertTrue(option1InCmd.isSet());
        Option option2InCmd = parsedCmd.getOption("option2");
        assertNotNull(option2InCmd);
        assertTrue(option2InCmd.isSet());
    }

    /**
     * Test the {@code parse} method with missing required options.
     */
    @Test
    public void testParseMissingRequiredOptions()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 with value");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {};
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }

    /**
     * Test the {@code parse} method with missing required options and extra arguments.
     */
    @Test
    public void testParseMissingRequiredOptionsWithExtraArguments()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 with value");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"arg1", "arg2"};
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }

    /**
     * Test the {@code parse} method with missing required options and an unrecognized option.
     */
    @Test
    public void testParseMissingRequiredOptionsWithUnrecognizedOption()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 with value");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--unrecognized", "arg1"};
        assertThrows(UnrecognizedOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }

    /**
     * Test the {@code parse} method with missing required options and an option with value.
     */
    @Test
    public void testParseMissingRequiredOptionsWithOptionValue()
    {
        Options options = new Options();
        Option option1 = new Option("o1", "option1", true, "option1 with value");
        Option option2 = new Option("o2", "option2", true, "option2 with value");
        options.addOption(option1);
        options.addOption(option2);
        String[] arguments = {"--option2=value", "arg1"};
        assertThrows(MissingOptionException.class, () -> new DefaultParser().parse(options, arguments));
    }
}